package com.dongnao.jack.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;


@Entity
@Table ( name = "accesstoken" )
@NamedQuery ( name = "Accesstoken.findAll", query = "SELECT a FROM Accesstoken a" )
public class Accesstoken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue ( strategy = GenerationType.IDENTITY )
	private long id;

	private String accessToken;

	private Date creattime;

	private int expiresIn;

	private Date logintime;

	private String loginuser;

	private long loginuserId;

	private String veriMobile;

	private String vericode;

	private Date vericodeExpired;

	private String veribusiness;

	private String deviceToken;

	private String system;

	private String version;

	private short status = 1;
	
	/**
	 * 当前用户操作的企业ID
	 */
	@Column(columnDefinition="bigint(15) default 0",nullable=true)
	private long companyId;

	public Accesstoken( ) {
	}

	@Column ( name = "device_token", nullable = true, length = 255 )
	public String getDeviceToken( ) {
		return deviceToken;
	}

	public void setDeviceToken( String deviceToken ) {
		this.deviceToken = deviceToken;
	}

	@Column ( name = "system", nullable = true, length = 255 )
	public String getSystem( ) {
		return system;
	}

	public void setSystem( String system ) {
		this.system = system;
	}

	@Column ( name = "version", nullable = true, length = 50 )
	public String getVersion( ) {
		return version;
	}

	public void setVersion( String version ) {
		this.version = version;
	}

	@Id
	@Column ( unique = true, nullable = false )
	public long getId( ) {
		return this.id;
	}

	public void setId( long id ) {
		this.id = id;
	}

	@Column ( name = "access_token", nullable = false, length = 64 )
	public String getAccessToken( ) {
		return this.accessToken;
	}

	public void setAccessToken( String accessToken ) {
		this.accessToken = accessToken;
	}

	@Temporal ( TemporalType.TIMESTAMP )
	@Column ( nullable = false )
	public Date getCreattime( ) {
		return this.creattime;
	}

	public void setCreattime( Date creattime ) {
		this.creattime = creattime;
	}

	@Column ( name = "expires_in", nullable = false )
	public int getExpiresIn( ) {
		return this.expiresIn;
	}

	public void setExpiresIn( int expiresIn ) {
		this.expiresIn = expiresIn;
	}

	@Temporal ( TemporalType.TIMESTAMP )
	public Date getLogintime( ) {
		return this.logintime;
	}

	public void setLogintime( Date logintime ) {
		this.logintime = logintime;
	}

	@Column ( length = 32 )
	public String getLoginuser( ) {
		return this.loginuser;
	}

	public void setLoginuser( String loginuser ) {
		this.loginuser = loginuser;
	}

	@Column ( length = 16 )
	public String getVericode( ) {
		return vericode;
	}

	public void setVericode( String vericode ) {
		this.vericode = vericode;
	}

	@Temporal ( TemporalType.TIMESTAMP )
	public Date getVericodeExpired( ) {
		return vericodeExpired;
	}

	public void setVericodeExpired( Date vericodeExpired ) {
		this.vericodeExpired = vericodeExpired;
	}

	@Column ( length = 16 )
	public String getVeriMobile( ) {
		return veriMobile;
	}

	public void setVeriMobile( String veriMobile ) {
		this.veriMobile = veriMobile;
	}

	@Column ( length = 50 )
	public String getVeribusiness( ) {
		return veribusiness;
	}

	public void setVeribusiness( String veribusiness ) {
		this.veribusiness = veribusiness;
	}

	public long getLoginuserId( ) {
		return loginuserId;
	}

	public void setLoginuserId( long loginuserId ) {
		this.loginuserId = loginuserId;
	}

	public short getStatus( ) {
		return status;
	}

	public void setStatus( short status ) {
		this.status = status;
	}
	
	

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	@Transient
	public boolean verifyMobileCode( String code, String business ) {
		if( StringUtils.isBlank( this.vericode ) )
			return false;
		if ( this.getVericodeExpired( ).getTime( ) < System.currentTimeMillis( ) )
			return false;
		if ( code.equals( this.vericode ) && business.equals( this.veribusiness ) )
			return true;
		return false;
	}

	public String toString( ) {
		return ReflectionToStringBuilder.toString( this );
	}

}