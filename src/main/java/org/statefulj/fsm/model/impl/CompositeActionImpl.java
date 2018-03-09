/***
 * 
 * Copyright 2014 Andrew Hall
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.statefulj.fsm.model.impl;

import java.util.List;

import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.model.Action;

/**
 * A "composite" Action which is composed of a set of {@link org.statefulj.fsm.model.Action}.  When invoked,
 * it will iterate and invoke all the composition Actions.
 * 
 * @author Andrew Hall
 *
 * @param <T> The class of the Stateful Entity
 */
public class CompositeActionImpl<T> implements Action<T> {

	List<Action<T>> actions;
	
	public CompositeActionImpl(List<Action<T>> actions) {
		this.actions = actions;
	}
	
	public void execute(T stateful, String event, Object ... args) throws RetryException{
		for(Action<T> action : this.actions) {
			action.execute(stateful, event, args);
		}
	}

}
