package org.soadecoder.extensions.internal;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Deactivate;
import org.wso2.carbon.identity.event.handler.AbstractEventHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soadecoder.extensions.CrackedPasswordValidator;


@Component(name = "org.soadecoder.extensions.internal.ServiceComponent",service= ServiceComponent.class , immediate=true)
public class ServiceComponent {

    private static final Log log = LogFactory.getLog(ServiceComponent.class);

    @Activate
    protected void activate(ComponentContext context) {

        try {

            if (log.isDebugEnabled()) {
                log.debug("service component is enabled");
            }
            BundleContext bundleContext = context.getBundleContext();

            log.info("Activated Cracked Password Validator");

            CrackedPasswordValidator handler = new CrackedPasswordValidator();
            bundleContext.registerService(AbstractEventHandler.class.getName(), handler, null);

            DataHolder.watchFile();


        } catch (Throwable e) {
            log.error("Error while activating cracked password validator component.", e);
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        if (log.isDebugEnabled()) {
            log.debug("cracked password validator bundle is de-activated");
        }
    }
}
