import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.xml.sax.InputSource;
 

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
		sOutFile = "c:\\tmp\\test_XmlWrite.xml" ;

		/*
		test_XmlWrite (sOutFile) ;
		test_ReadXml (sOutFile) ;
		*/
		//test_Object_XSER () ;
		
		XmlUtils xu = new XmlUtils () ; 
				
		Object_XSER_TestClass oxtc = xu.new Object_XSER_TestClass () ;
		String sXML = oxtc.sSaveToXml() ;
		FileUtils.bDumpStrIntoFile(sOutFile, sXML) ;
		
		Object_XSER_TestClass oxtcRead = xu.new Object_XSER_TestClass () ;
		oxtcRead.LoadFromXml(sXML);
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
	//////////////////////////////////////
	//Classes to manage DOM XML serialization of based types.
	//
	//http://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html
	//
	public static void test_Object_XSER ()
	{
		String s = "" ;
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootEle = doc.createElement("TestDataObject");
			Node rootNode = (Node)rootEle ;
			doc.appendChild(rootNode);
			
			/////////////////////////////
			XmlUtils xu = new XmlUtils () ;
			String_XSER ssTest1 = xu.new String_XSER ("ssTest1", "ssTest1 Val") ;
			ssTest1.SaveToXml(rootNode);
			/////////////////////////////

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StringWriter swXml = new StringWriter () ;
			StreamResult result = new StreamResult(swXml);
			
			/////
			//write out the XML so readable in text editor per this:
			//http://stackoverflow.com/questions/161462/java-writing-a-dom-to-an-xml-file-formatting-issues
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			/////
			
			transformer.transform(source, result);
			String sXml = swXml.toString() ;

			StringReader srRdr = new StringReader (sXml) ;
			InputSource isRdr = new InputSource(srRdr);
			Document docRead = docBuilder.parse(isRdr);
			docRead.normalize();

			docRead.getDocumentElement().normalize();
			Node nRootRead = docRead.getDocumentElement() ;			
			String_XSER ssTest1_read = xu.new String_XSER ("ssTest1", "ssTest1 Val") ;
			s = ssTest1_read.sReadFromXml(nRootRead) ;
			
		}
		catch (Exception exp)
		{
			
		}
	}
	//////////////////////////////////
	//
	public class Object_XSER_Mgr
	{
		private String m_sClassName = "" ;
		
		private String m_sInputXml = "" ;
		private Document m_docRead = null ;
		private Node m_nRootRead = null ;

		/*
		private List <Object_XSER> m_lstObjs = new ArrayList <Object_XSER>() ;
		*/
		
		//example from http://stackoverflow.com/questions/6886712/c-sharp-to-java-dictionaries
		//
		//iterating HashMap: http://stackoverflow.com/questions/1066589/java-iterate-through-hashmap
		//
		private Map<String, Object_XSER> m_mapObjs = new HashMap <String, Object_XSER>() ;
		
		private XmlUtils m_xu = new XmlUtils () ; 
		
		public Object_XSER_Mgr (String sClassName, String sXmlInput)
		{
			String sFN = TraceUtils.sGetFN() ;
			
			m_sClassName = sClassName ;
			m_sInputXml = sXmlInput ;
			if (m_sInputXml != "")
			{
				//XML image provided in contructor, load it to read vars later.
				try
				{
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
					
					StringReader srRdr = new StringReader (m_sInputXml) ;
					InputSource isRdr = new InputSource(srRdr);
					m_docRead = docBuilder.parse(isRdr);
					m_docRead.normalize();
	
					m_docRead.getDocumentElement().normalize();
					Node nRootRead = m_docRead.getDocumentElement() ;
					m_nRootRead = nRootRead ;
				}
				catch (Exception exp)
				{
					TraceUtils.Trc(sFN + " exception " + exp.getMessage());
				}
			}
		}
		public void AddStr (String sVarName, String sVal)
		{
			String_XSER sx = m_xu.new String_XSER (sVarName, sVal) ;
			m_mapObjs.put(sVarName,  sx) ;
		}
		public String sGetStr (String sVarName)
		{
			String_XSER sx = m_xu.new String_XSER (sVarName, "") ;
			return sx.sReadFromXml(m_nRootRead) ;
		}
		public void Addint (String sVarName, int iVal)
		{
			int_XSER ix = m_xu.new int_XSER (sVarName, iVal) ;
			m_mapObjs.put(sVarName, ix) ;
		}
		public int iGetint (String sVarName)
		{
			int_XSER ix = new int_XSER (sVarName, -1) ;
			return ix.iReadFromXml(m_nRootRead) ;
		}
		public void Addbool (String sVarName, boolean bVal)
		{
			bool_XSER bx = m_xu.new bool_XSER (sVarName, bVal) ;
			m_mapObjs.put(sVarName, bx) ;
		}
		public boolean bGetbool (String sVarName)
		{
			bool_XSER bx = new bool_XSER (sVarName, false) ;
			return bx.bReadFromXml(m_nRootRead) ;
		}
		public void AddStringList (String sVarName, List<String>lstStrs)
		{
			StringList_XSER slx = new StringList_XSER (sVarName, lstStrs) ;
			m_mapObjs.put(sVarName, slx) ;
		}
		public List<String> lstGetStringList (String sVarName)
		{
			StringList_XSER slx = new StringList_XSER (sVarName, null) ;
			return slx.lstReadFromXml(m_nRootRead) ;
		}
		public String sSaveToXml ()
		{
			String sr = "" ;
			Exception expThrown = null ;
			try
			{
				/////////////////////////////
				//Init XML
				//
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		 
				// root elements
				Document doc = docBuilder.newDocument();
				Element rootEle = doc.createElement(m_sClassName);
				Node rootNode = (Node)rootEle ;
				doc.appendChild(rootNode);

				////////////////////////////
				//Add data
				//
				Iterator it = m_mapObjs.entrySet().iterator();
			    while (it.hasNext()) 
			    {
			        Map.Entry<String, Object_XSER> pairs = (Map.Entry<String, Object_XSER>)it.next();
			        Object_XSER os = (Object_XSER)pairs.getValue() ;
			        os.SaveToXml(rootNode);
			        
			        it.remove(); // avoids a ConcurrentModificationException
			    }				
			    /*
				for (int iOs=0; iOs<m_lstObjs.size(); iOs++)
				{
					Object_XSER os = m_lstObjs.get(iOs) ;
					os.SaveToXml(doc, rootNode);
				}
				*/
				
				////////////////////////////
				//Write XML
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);

				StringWriter swXml = new StringWriter () ;
				StreamResult result = new StreamResult(swXml);
				
				/////
				//write out the XML so readable in text editor per this:
				//http://stackoverflow.com/questions/161462/java-writing-a-dom-to-an-xml-file-formatting-issues
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				/////
				
				transformer.transform(source, result);
				String sXml = swXml.toString() ;
				sr = sXml ;
			}
			catch (Exception exp)
			{
				expThrown = exp ;
			}
			return sr ;
		}
	}
	//////////////////////////////////
	//Class to prototype XML serializable classes
	//
	public class Object_XSER_TestClass
	{
		public String m_sVal1 = "m_sVal1 value" ;
		public String m_sVal2 = "m_sVal2 value" ;
		public int m_iVal1 = 1 ;
		public boolean m_bVal1 = true ;
		public List <String> m_lstStrs = new ArrayList<String>() ;
		
		private XmlUtils m_xu = new XmlUtils () ;
		
		public Object_XSER_TestClass ()
		{
			m_lstStrs.add("A string") ;
			m_lstStrs.add("another string") ;
			m_lstStrs.add("line3") ;
			m_lstStrs.add("line4 \n with linefeed") ;
		}
		
		public String sSaveToXml ()
		{
			String sr = "" ;
			try
			{
				Object_XSER_Mgr m_XSER_Mgr = m_xu.new Object_XSER_Mgr ("Object_XSER_TestClass", "") ;
				
				//Load our variables into the XML serializer
				m_XSER_Mgr.AddStr("m_sVal1", m_sVal1);
				m_XSER_Mgr.AddStr("m_sVal2", m_sVal2);
				m_XSER_Mgr.Addint("m_iVal1", m_iVal1);
				m_XSER_Mgr.Addbool("m_bVal1", m_bVal1);
				m_XSER_Mgr.AddStringList("m_lstStrs", m_lstStrs);

				sr = m_XSER_Mgr.sSaveToXml() ;
			}
			catch (Exception exp)
			{
			}
			
			return sr ;
		}
		public void LoadFromXml (String sXML)
		{
			String sr = "" ;
			try
			{
				Object_XSER_Mgr m_XSER_Mgr = m_xu.new Object_XSER_Mgr ("Object_XSER_TestClass", sXML) ;
				
				//Load our variables into the XML serializer
				m_sVal1 = m_XSER_Mgr.sGetStr("m_sVal1");
				m_sVal2 = m_XSER_Mgr.sGetStr("m_sVal2");
				m_iVal1 = m_XSER_Mgr.iGetint("m_iVal1");
				m_bVal1 = m_XSER_Mgr.bGetbool("m_bVal1");
				m_lstStrs = m_XSER_Mgr.lstGetStringList("m_lstStrs");
			}
			catch (Exception exp)
			{
			}
		}
	}
	public class Object_XSER
	{
		//Types
		public static final String StringType = "String" ;
		public static final String intType = "int" ;
		public static final String boolType = "bool" ;
		public static final String StringListType = "StringList" ;
		
		//other consts
		/*on second thought, store this as a text node
		public static final String XmlValueAttribName = "value" ;
		*/
		public static final String XmlTypeAttribName = "type" ;
		
		protected String m_sValName = "" ;
		protected String m_sValType = "" ;
		protected String m_sVal = "" ;
		
		public Object_XSER (String sValName, String sValType, String sVal)
		{
			m_sValName = sValName ;
			m_sValType = sValType ;
			m_sVal = sVal ;
		}
		public Node SaveToXml (Node node)
		{
			Node nr = null ;
			
			Document doc = node.getOwnerDocument() ;
			if (doc == null)
			{
				doc = (Document)node ;
			}
			
			Element eleNew = doc.createElement(m_sValName);
			node.appendChild(eleNew);
	 
			Attr attr = null ;

			attr = doc.createAttribute(XmlTypeAttribName);
			attr.setValue(m_sValType);
			eleNew.setAttributeNode(attr);
			
			/*save as text node
			attr = doc.createAttribute(XmlValueAttribName);
			attr.setValue(m_sVal);
			eleNew.setAttributeNode(attr);
			
			e.g.
			Element firstname = doc.createElement("firstname");
			firstname.appendChild(doc.createTextNode("yong"));
			staff.appendChild(firstname);
			*/
			Node nText = doc.createTextNode(m_sVal) ;
			eleNew.appendChild(nText) ;
			
			nr = eleNew ;
			return nr ;
		}
		public Node nFindValNode (Node node)
		{
			Node nr = null ;
			NodeList nlChildren = node.getChildNodes() ;
			if (nlChildren != null)
			{
				for (int iChild=0; iChild<nlChildren.getLength(); iChild++)
				{
					Node nChild = nlChildren.item(iChild) ;
					if (nChild.getNodeType() == Node.ELEMENT_NODE)
					{
						String sCurNodeName = nChild.getNodeName() ; 
						if (sCurNodeName.equals(m_sValName))
						{
							nr = nChild ;
							break ;
						}
					}
				}
			}
			return nr ;
		}
		public String sReadFromXml (Node node)
		{
			String sr = "" ;
			
			Node nVal = nFindValNode (node) ;
			/*
			sr = nVal.getAttributes().getNamedItem(XmlValueAttribName).getNodeValue() ;
			*/
			if (nVal != null)
			{
				sr = nVal.getTextContent() ;
			}
			return sr ;
		}
		public String sReadTypeFromXml (Node node)
		{
			String sr = "" ;
			Node nVal = nFindValNode (node) ;
			sr = nVal.getAttributes().getNamedItem(XmlTypeAttribName).getNodeValue() ;
			return sr ;
		}
	}
	public class String_XSER extends Object_XSER 
	{
		public String_XSER (String sValName, String sVal)
		{
			super (sValName, Object_XSER.StringType, sVal) ;
		}
	}
	public class int_XSER extends Object_XSER 
	{
		public int_XSER (String sValName, int iVal)
		{
			super (sValName, Object_XSER.intType, "") ;
			m_sVal = Integer.toString(iVal) ;
		}
		public int iReadFromXml (Node node)
		{
			String sr = sReadFromXml (node).trim() ;
			
			int ir = Integer.parseInt(sr);
			
			return ir ;
		}
	}
	public class bool_XSER extends Object_XSER 
	{
		public bool_XSER (String sValName, Boolean bVal)
		{
			super (sValName, Object_XSER.boolType, "") ;
			m_sVal = String.valueOf(bVal) ;
		}
		public Boolean bReadFromXml (Node node)
		{
			String sr = sReadFromXml (node) ;
			Boolean br = Boolean.valueOf(sr) ;
			return br ;
		}
	}
	public class StringList_XSER extends Object_XSER
	{
		private List<String> m_lstStrs = null ;
		
		public StringList_XSER (String sValName, List<String>lstStrs)
		{
			super (sValName, Object_XSER.StringListType, "") ;
			m_lstStrs = lstStrs ;
			
			int iNumValues = 0 ;
			if (m_lstStrs != null)
			{
				iNumValues = m_lstStrs.size() ;
			}
			m_sVal = Integer.toString(iNumValues) ;
		}
		public Node SaveToXml (Node node)
		{
			Node nr = null ;
			
			Document doc = node.getOwnerDocument() ;
			if (doc == null)
			{
				doc = (Document)node ;
			}
			
			Node nRoot = super.SaveToXml(node) ;

			for (int iStrs=0; iStrs<m_lstStrs.size(); iStrs++)
			{
				String sSubVal = m_lstStrs.get(iStrs) ;
				String sSubValName = String.format("%s%d", 
						m_sValName,
						iStrs) ;
				String_XSER sx = new String_XSER (sSubValName, sSubVal) ;
				sx.SaveToXml(nRoot) ;
			}
			
			nr = nRoot ;
			return nr ;
		}
		public List<String>lstReadFromXml (Node node)
		{
			List<String> lstr = new ArrayList<String>() ;
			
			Node nRootVal = super.nFindValNode(node) ;
			if (nRootVal == null)
			{
				return lstr ;
			}
			
			////////
			//new more flexible way, iterate the number specified in the root
			//
			Node nFirst = nRootVal.getFirstChild() ;
			if (nFirst == null)
			{
				//no count saved in XML.
				return lstr ;
			}
			//Get the count of child elements.
			String sChildren = nFirst.getTextContent() ;
			int iChildren = Integer.parseInt(sChildren) ;
			
			//load each child.
			for (int iCurChild=0; iCurChild < iChildren; iCurChild++)
			{
				String sCurChild = String.format("%s%d", m_sValName, iCurChild) ;
				String_XSER sxCur = new String_XSER (sCurChild, "") ;
				String sCur = sxCur.sReadFromXml(nRootVal) ;
				lstr.add(sCur) ;
			}

			/*last way, iterate children of root.
			NodeList nlChildren = nRootVal.getChildNodes() ;
			if (nlChildren != null)
			{
				for (int iChild=0; iChild<nlChildren.getLength(); iChild++)
				{
					Node nChild = nlChildren.item(iChild) ;
					if (nChild.getNodeType() == Node.ELEMENT_NODE)
					{
						if (nChild.getNodeName().startsWith(m_sValName))
						{
							String sVal = "" ;
							
							sVal = nChild.getTextContent() ;
							
							lstr.add(sVal) ;
						}
					}
				}
			}
			*/
			return lstr ;
		}
	} 
	
}
