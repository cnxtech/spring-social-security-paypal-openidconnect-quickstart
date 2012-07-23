/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.quickstart;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.openidconnect.api.PayPal;
import org.springframework.social.openidconnect.api.PayPalProfile;
import org.springframework.social.quickstart.user.BMLUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Simple little @Controller that invokes PayPal and renders the result. The injected {@link PayPal}
 * reference is configured with the required authorization credentials for the current user behind
 * the scenes.
 * 
 * @author Keith Donald, adapted to PayPal by Felipe Albertao
 */
@Controller
public class HomeController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String popUp() {
		return "popupHandler";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {
		PayPalProfile paypalProfile = ((BMLUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPayPalUser();
		model.addAttribute("profile", paypalProfile);
		return "home";
	}

}