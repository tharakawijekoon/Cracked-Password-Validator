package org.soadecoder.extensions;

import org.soadecoder.extensions.internal.DataHolder;
import org.wso2.carbon.identity.event.handler.AbstractEventHandler;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.wso2.carbon.identity.event.IdentityEventConstants;
import org.wso2.carbon.identity.core.bean.context.MessageContext;
import org.wso2.carbon.identity.base.IdentityException;



public class CrackedPasswordValidator extends AbstractEventHandler
{

    private static final Log log = LogFactory.getLog(CrackedPasswordValidator.class);


    @Override
    public String getName() {
        return "crackedPasswordValidator";
    }

    @Override
    public void handleEvent(Event event) throws IdentityEventException {

        if (log.isDebugEnabled()) {
            log.debug("Cracked Password validator invoked");
        }

        if (IdentityEventConstants.Event.PRE_UPDATE_CREDENTIAL.equals(event.getEventName()) || IdentityEventConstants.Event
                .PRE_UPDATE_CREDENTIAL_BY_ADMIN.equals(event.getEventName()) || IdentityEventConstants.Event.PRE_ADD_USER.equals(event.getEventName())) {
            Object credential = event.getEventProperties().get(IdentityEventConstants.EventProperty.CREDENTIAL);
            try {
                boolean validate = (DataHolder.getLines()).contains(credential.toString());
                if (validate) {
                    if (log.isDebugEnabled()) {
                        log.debug("Password in cracked file Password:"+credential);
                    }
                    throw IdentityException.error(IdentityEventException.class, "40002", "This password has been cracked. Please choose a different password");
                }
            } catch (Exception e) {
                throw IdentityException.error(IdentityEventException.class, "40002", "This password has been cracked. Please choose a different password",e);
            }
        }

    }

    @Override
    public int getPriority(MessageContext messageContext) {
        return 50;
    }


}
