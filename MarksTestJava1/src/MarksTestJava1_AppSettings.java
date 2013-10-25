
/*
 * ini4j
 * http://stackoverflow.com/questions/193474/how-to-create-an-ini-file-to-store-some-settings-in-java
 * http://ini4j.sourceforge.net/apidocs/index.html
 */
import org.ini4j.* ;

import java.util.prefs.* ;
import java.io.File ;

public class MarksTestJava1_AppSettings 
{
	public String m_sSetting1 = "" ;
	public int m_iSetting2 = 1 ;
	
	public static String SETTINGS = "SETTINGS" ;
	
	public MarksTestJava1_AppSettings ()
	{
		
	}
	/////////////////////////////////////////////
	//
	public void CheckMe () throws Exception
	{
		String sErrs = "" ;
		String sErr = "" ;
		if (m_sSetting1 == "")
		{
			sErr = "Setting1 not set" ;
			sErrs = BufUtils.AppendStr(sErrs,  sErr) ;
		}
		
		if (sErrs != "")
		{
			throw new Exception (sErrs) ;
		}
	}
	public void ReadSettingsFromIni ()
	{
		String sIni = AppState.m_AppDetails.m_sIni ;

		File fIni = null ;
		try
		{
			//http://ini4j.sourceforge.net/sample/ReadPrimitiveSample.java.html
			fIni = new File (sIni) ;
			if (!fIni.exists())
			{
				//no ini file
				return ;
			}
			Ini iniObj = new Ini (fIni) ;
			IniPreferences iniP = new IniPreferences (iniObj) ;
			Preferences iniPsec = iniP.node(SETTINGS) ;
			

			m_sSetting1 = iniPsec.get("Setting1", m_sSetting1) ;
			m_iSetting2 = iniPsec.getInt("Setting2", m_iSetting2) ;
			
			//m_sSetting1 = ini.getString(SETTINGS, "Setting1", m_sSetting1) ;
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
	}
	public void WriteSettingsToIni ()
	{
		String sIni = AppState.m_AppDetails.m_sIni ;

		File fIni = null ;
		try
		{
			//http://ini4j.sourceforge.net/sample/ReadPrimitiveSample.java.html
			fIni = new File (sIni) ;
			if (!fIni.exists())
			{
				fIni.createNewFile() ;
			}
			fIni.setWritable(true) ;
			Ini iniObj = new Ini (fIni) ;
			IniPreferences iniP = new IniPreferences (iniObj) ;
			Preferences iniPsec = iniP.node(SETTINGS) ;

			iniPsec.put("Setting1", m_sSetting1);
			iniPsec.putInt("Setting2", m_iSetting2);

			iniObj.store();
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
	}
	
}
