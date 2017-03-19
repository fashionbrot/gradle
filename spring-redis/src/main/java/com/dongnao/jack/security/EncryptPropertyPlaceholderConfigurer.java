package com.dongnao.jack.security;

import java.io.File;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.dongnao.jack.util.DesUtils;
import com.dongnao.jack.util.MyFileUtils;


public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	protected void processProperties( ConfigurableListableBeanFactory beanFactory, Properties props ) throws BeansException {
		try {

			String thekey = FileUtils.readFileToString( new File( MyFileUtils.DB_KEY_FILE ) );
			for ( Object obj : props.keySet( ) ) {
				String key = ( String ) obj;
				if ( !key.startsWith( "jdbc_key" ) )
					continue;
				String password = props.getProperty( key );
				if ( password != null ) {
					props.setProperty( key, DesUtils.decrypt( password, thekey.getBytes( "UTF-8" ) ) );
				}

			}

			super.processProperties( beanFactory, props );
		} catch ( Exception e ) {
			e.printStackTrace( );
			throw new BeanInitializationException( e.getMessage( ) );
		}
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(DesUtils.decrypt("123456", "vbGl!a@RaPuIxS(BaFGG".getBytes( "UTF-8" )));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
