import java.io.File;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
 

public class XmlUtils 
{

	public static void main(String[] args) 
	{
		AppState.initAppDetails(XmlUtils.class);
		test () ;
	}
	////////////////////////////////////////
	//
	public static void test ()
	{
		String sOutFile = "C:\\file.xml" ;
		sOutFile = AppState.m_AppDetails.m_sMainClassDir + "\\test_XmlWrite.xml" ;

		test_XmlWrite (sOutFile) ;
		test_ReadXml (sOutFile) ;
	}
	/////////////////////////////////////////
	//
	//example code from http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
	/*
	writes out this XML:
	
	<?xml version="1.0" encoding="UTF-8" standalone="no"?>
	<company>
		<Staff id="1">
			<firstname>yong</firstname>
			<lastname>mook kim</lastname>
			<nickname>mkyong</nickname>
			<salary>100000</salary>
		</Staff>
	</company>
	
	*/
	//
	public static void test_XmlWrite (String sOutFile)
	{
	  try 
	  {
		  if (FileUtils.bFileExists(sOutFile))
		  {
			  //already created
			  return ; 
		  }
		  
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("company");
		doc.appendChild(rootElement);
 
		// staff elements
		Element staff = doc.createElement("Staff");
		rootElement.appendChild(staff);
 
		// set attribute to staff element
		Attr attr = doc.createAttribute("id");
		attr.setValue("1");
		staff.setAttributeNode(attr);
 
		// shorten way
		// staff.setAttribute("id", "1");
 
		// firstname elements
		Element firstname = doc.createElement("firstname");
		firstname.appendChild(doc.createTextNode("yong"));
		staff.appendChild(firstname);
 
		// lastname elements
		Element lastname = ((org.w3c.dom.Document) doc).createElement("lastname");
		lastname.appendChild(doc.createTextNode("mook kim"));
		staff.appendChild(lastname);
 
		// nickname elements
		Element nickname = doc.createElement("nickname");
		nickname.appendChild(doc.createTextNode("mkyong"));
		staff.appendChild(nickname);
 
		// salary elements
		Element salary = doc.createElement("salary");
		salary.appendChild(doc.createTextNode("100000"));
		staff.appendChild(salary);
 
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
	
		/*
		//write to a file
		StreamResult result = new StreamResult(new File(sOutFile));
		
 
		// Output to console for testing
		//StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
 
		System.out.println("File saved!");
		*/
		
		//testing write to a string
		StringWriter swXml = new StringWriter () ;
		StreamResult result = new StreamResult(swXml);
		
		/////
		//write out the XML so readable in text editor per this:
		//http://stackoverflow.com/questions/161462/java-writing-a-dom-to-an-xml-file-formatting-issues
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		/////
		
		transformer.transform(source, result);
		String sXml = swXml.toString() ;
 
		//now dump the string to a file.
		FileUtils.bDumpStrIntoFile(sOutFile, sXml) ;
		
		//test reading the XML.
		test_ReadXml (sOutFile) ;
	  } catch (ParserConfigurationException pce) {
		  pce.printStackTrace();
	  } catch (TransformerException tfe) {
		  tfe.printStackTrace();
	  }		

	}
	//////////////////////////
	//
	//example from http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
	//
	public static void test_ReadXml (String sXmlFile)
	{
		String sFN = TraceUtils.sGetFN() ;
		String sLog = "" ;
		boolean b = false ;
		try
		{
			File fXmlFile = new File(sXmlFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.normalize();
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			Node nRoot = doc.getDocumentElement() ;
			
			WalkXmlNodes (nRoot) ;
			
			/*
			//TraceUtils.Trc("Root element :" + doc.getDocumentElement().getNodeName());
			sLog = "Root is " + nRoot.getNodeName() ;
			
			b = nRoot.hasChildNodes() ;
			NodeList nlstRoot = nRoot.getChildNodes() ;
			for (int inlstRoot = 0; inlstRoot<nlstRoot.getLength(); inlstRoot++)
			{
				Node nCurChild = nlstRoot.item(inlstRoot);
				sLog = String.format("Root child node %d: %s, %s", 
						inlstRoot,
						nCurChild.getNodeName(),
						nCurChild.getNodeValue()) ;
			}
			
			Node nRootChild = nRoot.getFirstChild() ;
			sLog = "Root child is " + nRootChild.getNodeName() ;
			
			Node nRootChildChild = nRootChild.getFirstChild() ;
			sLog = "Root Child Child is " + nRootChildChild.getNodeName() ;
		 
			NodeList nList = doc.getElementsByTagName("staff");
		 
			TraceUtils.Trc("----------------------------");
		 
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 
				TraceUtils.Trc("Current Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
		 
					TraceUtils.Trc("Staff id : " + eElement.getAttribute("id"));
					TraceUtils.Trc("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
					TraceUtils.Trc("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
					TraceUtils.Trc("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
					TraceUtils.Trc("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
				}
			}
			*/
		}
		catch (Exception exp)
		{
			sLog = sFN + " exception " + exp.getMessage() ;
			TraceUtils.Trc(sLog);
		}
	}
	///////////////////////////////
	//
	//example from http://stackoverflow.com/questions/5386991/java-most-efficient-method-to-iterate-over-all-elements-in-a-org-w3c-dom-docume
	//
	public static String sGetXmlNodePath (Node node)
	{
		boolean b = false ;
		Node nCur = node ;
		
		String sPath = "" ;
		String sCurName = nCur.getNodeName() ;
		sPath = sCurName ;
		
		nCur = nCur.getParentNode() ;
		while (nCur != null)
		{
			sCurName = nCur.getNodeName() ;
			
			sPath = sCurName + "\\" + sPath ;
			
			nCur = nCur.getParentNode() ;
		}
		
		return sPath ;
	}
	public static void WalkXmlNodes(Node node) 
	{
		
		String s = "" ;
		
	    // do something with the current node instead of System.out
	    //System.out.println(node.getNodeName());
		
		String sNodePath = sGetXmlNodePath(node) ;
		System.out.println(sNodePath);

	    NodeList nodeList = node.getChildNodes();
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        Node currentNode = nodeList.item(i);
	        
	        int iNodeType = currentNode.getNodeType() ;
	        
	        //http://xerces.apache.org/xerces2-j/javadocs/api/constant-values.html#org.w3c.dom.Node.ELEMENT_NODE
	        if (iNodeType == Node.ELEMENT_NODE) 
	        {
	            //calls this method for all the children which is Element
	        	WalkXmlNodes(currentNode);
	        }
	        else
	        {
	        	if (iNodeType == Node.TEXT_NODE)
	        	{
	        		String sTextNodeVal = currentNode.getTextContent().trim() ;
	        		if (!BufUtils.bStrIsEmpty(sTextNodeVal))
	        		{
	        			String sPathWVal = sNodePath + "=" + sTextNodeVal ;
	        			System.out.println(sPathWVal);
	        		}
	        	}
	        	s = "Skipping" ;
	        }
	    }
	}	
}
