import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.naming.ldap.LdapName ;
import javax.naming.ldap.Rdn;

public class CryptUtils 
{
	public static void main(String[] args) throws Exception 
	{
		AppState.initAppDetails(JavaTests.class); //call this before anything!
		
		String sFN = TraceUtils.sGetFN () ;
		AppLog.Log(sFN + " initialized");

		//test1 () ;
		TraceWindowsCertStore () ;
		
		AppLog.Log(sFN + " exited");
	}
    //////////////////////////////////
	//
	public static String sGetCN (String sDN)
	{
		String sFN = TraceUtils.sGetFN() ;
		String sr = "" ;
		
		try
		{
			//http://stackoverflow.com/questions/7933468/parsing-the-cn-out-of-a-certificate-dn
			LdapName ln = new LdapName(sDN);
	
			for(Rdn rdn : ln.getRdns()) 
			{
			    if(rdn.getType().equalsIgnoreCase("CN")) 
			    {
			    	sr = rdn.getValue().toString() ;
			        break;
			    }
			}
		}
		catch (Exception exp)
		{
			TraceUtils.Trc(sFN + " exception " + exp.getMessage());
		}
		
		return sr ;
	}
	////////////////////////////////////////
	//
	public static void TraceWindowsCertStore ()
	{
		String sFN = TraceUtils.sGetFN() ;
		
		String sLog = "" ;
		try 
		{
			KeyStore keystore;
            // Get instance of the keystore
            //keystore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
			keystore = KeyStore.getInstance("Windows-MY");
             
            // Loading the keystore
            keystore.load(null, null);
            for (Enumeration oEnum = keystore.aliases(); oEnum.hasMoreElements();) 
            {                   
            	String sAlias = (String) oEnum.nextElement();
                X509Certificate oCert = (X509Certificate)keystore.getCertificate(sAlias);
                
                //http://www.java2s.com/Tutorial/Java/0490__Security/GettingtheSubjectandIssuerDistinguishedNamesofanX509Certificate.htm
                String sSubjDN = oCert.getSubjectDN().getName() ;
                String sSubjCN = sGetCN (sSubjDN) ;
                
                String sIssuerDN = oCert.getIssuerDN().getName () ;
                String sIssuerCN = sGetCN (sIssuerDN) ;
                
                PrivateKey oPrivateKey = (PrivateKey) keystore.getKey(sAlias,null);
                
                sLog = String.format("cert name: %s, subj: %s, issuer: %s", 
                		sAlias, 
                		sSubjCN,
                		sIssuerCN) ;
                if (oPrivateKey != null)
                {
                	sLog += " has private key" ;
                }
                AppLog.Log(sLog);
             }
            /*
	      } catch (KeyStoreException e) {                 
	             e.printStackTrace();
	      } catch (NoSuchProviderException e) {                  
	             e.printStackTrace();
	      } catch (NoSuchAlgorithmException e) {                 
	             e.printStackTrace();
	             */
	             /*
	      } catch (CertificateException e) {              
	             e.printStackTrace();
	             */
	             /*
	      } catch (IOException e) {                
	             e.printStackTrace();
	             */
            /*
	      } catch (UnrecoverableKeyException e) {                
	             e.printStackTrace();
	      }
	      */
		  /*
		  catch (InvalidKeyException e) {               
	             e.printStackTrace();
	      } catch (SignatureException e) {                
	             e.printStackTrace();
	      } 
	      */       
		  }
		  catch (Exception exp)
		  {
			  exp.printStackTrace();
		  }
	}
	
	public static void test1 ()
	{
		//http://muhammadhamed.blogspot.com/2010/04/accessing-ms-certificate-stores-in-java.html
		
		 try {
             KeyStore keystore;
             // Get instance of the keystore
             keystore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
             
             // Loading the keystore
             keystore.load(null, null);
             for (Enumeration oEnum = keystore.aliases(); oEnum.hasMoreElements();) 
             {                   
            	 String sAlias = (String) oEnum.nextElement();
                 X509Certificate oPublicCertificate = (X509Certificate) keystore
                                 .getCertificate(sAlias);                        
                   PrivateKey oPrivateKey = (PrivateKey) keystore.getKey(sAlias,null);
                   
                   // if no keys continue ..
                   if(oPrivateKey == null) continue;
                   
                   System.out.println("Found a private key with Alias name:"+sAlias);
                   
                   Provider p = keystore.getProvider();
                   
                   // data to signed
                   byte[] data ="this is the just for test".getBytes();
                   
                   // Signing the data
                   Signature sig = Signature.getInstance("SHA1withRSA");
                   sig.initSign(oPrivateKey);
                   
                   sig.update(data);
                   byte[] signature = sig.sign();
                   
                   Signature verifier = Signature.getInstance("SHA1withRSA", p);
                   verifier.initVerify(oPublicCertificate);
                   verifier.update(data);
                   System.out.println("the verification result "+verifier.verify(signature));
             }
	      } catch (KeyStoreException e) {                 
	             e.printStackTrace();
	      } catch (NoSuchProviderException e) {                  
	             e.printStackTrace();
	      } catch (NoSuchAlgorithmException e) {                 
	             e.printStackTrace();
	      } catch (CertificateException e) {              
	             e.printStackTrace();
	      } catch (IOException e) {                
	             e.printStackTrace();
	      } catch (UnrecoverableKeyException e) {                
	             e.printStackTrace();
	      } catch (InvalidKeyException e) {               
	             e.printStackTrace();
	      } catch (SignatureException e) {                
	             e.printStackTrace();
	      }             
	}
		

}
