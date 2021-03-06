/* ComponentScope.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Oct 19, 2009 6:54:49 PM, Created by henrichen
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 3.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/

package org.zkoss.spring.web.context.request;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.zkoss.spring.impl.ZKProxy;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.sys.ExecutionCtrl;

/**
 * ZK Component scope; accessible only in ZK event handling request.
 * 
 * @author henrichen
 * @since 1.2
 */
public class ComponentScope implements Scope {
	private static final String COMPONENT_SCOPE = "ZK_SPRING_COMPONENT_SCOPE";

	public Object get(String name, ObjectFactory<?> objectFactory) {
		final Execution exec = Executions.getCurrent();
		if (exec != null) {
			final Component self = ZKProxy.getProxy().getSelf((ExecutionCtrl)exec);
			if (self != null) {
				Map compScope = (Map) self.getAttribute(COMPONENT_SCOPE);
				if (compScope == null) {
					self.setAttribute(COMPONENT_SCOPE, compScope = new HashMap());
				}

				Object scopedObject = compScope.get(name);
				if (scopedObject == null) {
					scopedObject = objectFactory.getObject();
					compScope.put(name, scopedObject);
				}
				return scopedObject;
			}
		}
		throw new IllegalStateException("Unable to get component scope bean: "+name+". Do you access it in ZK event listener?");
	}

	public String getConversationId() {
		final Execution exec = Executions.getCurrent();
		if (exec != null) {
			final Component self = ZKProxy.getProxy().getSelf((ExecutionCtrl)exec);
			if (self != null)
				return self.getUuid();
		}
		return null;
	}

	public void registerDestructionCallback(String name, Runnable callback) {
		// do nothing
	}

	public Object remove(String name) {
		final Execution exec = Executions.getCurrent();
		if (exec != null) {
			final Component self = ZKProxy.getProxy().getSelf((ExecutionCtrl)exec);
			Map compScope = (Map) self.getAttribute(COMPONENT_SCOPE);
			return compScope != null ? compScope.remove(name) : null;
		}
		throw new IllegalStateException("Unable to remove component scope bean: "+name+". Do you access it in ZK event listener?");
	}
	
	public Object resolveContextualObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}
}
