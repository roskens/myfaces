/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.validate;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.sourceforge.myfaces.util.MessageUtils;

/**
 * @author mwessendorf
 *
 */
public class CreditCardValidator implements Validator,StateHolder {
	
	/**
	 * <p>The standard converter id for this converter.</p>
	 */
	public static final String 	VALIDATOR_ID 	   = "myfaces.validator.CreditCard";

	/**
	 * <p>The message identifier of the {@link FacesMessage} to be created if
	 * the creditcard check fails.</p>
	 */
	public static final String CREDITCATD_MESSAGE_ID = "myfaces.Creditcard.INVALID";	
	
	//private DEFAULT_VALUES
	private static final boolean DEFAULT_AMEX = true;
	private static final boolean DEFAULT_DISCOVER = true;
	private static final boolean DEFAULT_MASTERCARD = true;
	private static final boolean DEFAULT_VISA = true;
	private static final boolean DEFAULT_NONE = false;
	
	
	public CreditCardValidator(){
	}
	
	//Cardtypes, that are supported by Commons-Validator.
	private Boolean _amex = null;
	private Boolean _discover = null;
	private Boolean _mastercard = null;
	private Boolean _visa = null;
	private Boolean _none = null;
	
	//JSF-Field for StateHolder-IF
	private boolean _transient = false;
	
	//Field, to init the desired Validator
	private int _initSum = 0;

	private org.apache.commons.validator.CreditCardValidator creditCardValidator = null;

	/**
	 * 
	 */
	public void validate(
		FacesContext facesContext,
		UIComponent uiComponent,
		Object value)
		throws ValidatorException {

			if (facesContext == null) throw new NullPointerException("facesContext");
			if (uiComponent == null) throw new NullPointerException("uiComponent");

			if (value == null)
			{
				return;
			}
		initValidator();
		if (!this.creditCardValidator.isValid(value.toString())){
			throw new ValidatorException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR,CREDITCATD_MESSAGE_ID, null));
		}
	}


	// -------------------------------------------------------- Private Methods

	/**
	 * <p>initializes the desired validator.</p>
	 */

	private void initValidator() {
		if(isNone()){
			//no cardtypes are allowed
			creditCardValidator = new org.apache.commons.validator.CreditCardValidator(org.apache.commons.validator.CreditCardValidator.NONE);
		}
		else{
			computeValidators();
			creditCardValidator = new org.apache.commons.validator.CreditCardValidator(_initSum);
		}	
	}
	
	/**
	 * private methode, that counts the desired creditCards
	 */	
	private void computeValidators(){
		if(isAmex()){
			this._initSum= org.apache.commons.validator.CreditCardValidator.AMEX + _initSum;
		}
		if(isVisa()){
			this._initSum= org.apache.commons.validator.CreditCardValidator.VISA+ _initSum;	
		}
		if(isMastercard()){
			this._initSum= org.apache.commons.validator.CreditCardValidator.MASTERCARD+ _initSum;	
		}
		if(isDiscover()){
			this._initSum= org.apache.commons.validator.CreditCardValidator.DISCOVER+ _initSum;
		}
	}

	//GETTER & SETTER
	public boolean isAmex() {
		if (_amex!= null) return _amex.booleanValue();
		return _amex != null ? _amex.booleanValue() : DEFAULT_AMEX;
	}

	public boolean isDiscover() {
		if (_discover!= null) return _discover.booleanValue();
		return _discover != null ? _discover.booleanValue() : DEFAULT_DISCOVER;
	}

	public boolean isMastercard() {
		if (_mastercard!= null) return _mastercard.booleanValue();
		return _mastercard != null ? _mastercard.booleanValue() : DEFAULT_MASTERCARD;
	}

	public boolean isNone() {
		if (_none!= null) return _none.booleanValue();
		return _none != null ? _none.booleanValue() : DEFAULT_NONE;
	}

	public boolean isVisa() {
		if (_visa!= null) return _visa.booleanValue();
		return _visa != null ? _visa.booleanValue() : DEFAULT_VISA;
	}

	public void setAmex(boolean b) {
		_amex = Boolean.valueOf(b);
	}

	public void setDiscover(boolean b) {
		_discover = Boolean.valueOf(b);
	}

	public void setMastercard(boolean b) {
		_mastercard =  Boolean.valueOf(b);
	}

	public void setNone(boolean b) {
		_none =   Boolean.valueOf(b);
	}

	public void setVisa(boolean b) {
		_visa =   Boolean.valueOf(b);
	}

	/* (non-Javadoc)
	 * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
	 */
	public Object saveState(FacesContext context) {
		Object values[] = new Object[6];
		values[0] = _amex;
		values[1] = _discover;
		values[2] = _mastercard;
		values[3] = _visa;
		values[4] = _none;
		return values;
	}

	/* (non-Javadoc)
	 * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext, java.lang.Object)
	 */
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[])state;
		_amex = ((Boolean) values[0]);
		_discover = ((Boolean) values[1]);
		_mastercard = ((Boolean) values[2]);
		_visa = ((Boolean) values[3]);
		_none = ((Boolean) values[4]);
	}

	/* (non-Javadoc)
	 * @see javax.faces.component.StateHolder#isTransient()
	 */
	public boolean isTransient() {
		return _transient;
	}

	/* (non-Javadoc)
	 * @see javax.faces.component.StateHolder#setTransient(boolean)
	 */
	public void setTransient(boolean newTransientValue) {
		this._transient = newTransientValue;
	}
}
