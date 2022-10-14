/*
 *  Copyright (C) 2022 Starfire Aviation, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.starfireaviation.sms.controller;

import com.starfireaviation.sms.model.SMSMessage;
import com.starfireaviation.sms.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SMSController.
 */
@Slf4j
@RestController
@RequestMapping({
        "/sms"
})
public class SMSController {

    /**
     * SMSService.
     */
    private final MessageService messageService;

    /**
     * NotificationController.
     *
     * @param sService   SMSService
     */
    public SMSController(final MessageService sService) {
        messageService = sService;
    }

    /**
     * Receives an SMS message.
     *
     * @param message received
     * @return response
     */
    @PostMapping
    public String sms(@RequestBody final String message) {
        log.info(String.format("sms() called with [%s]", message));
        return messageService.receiveMessage(new SMSMessage(message));
    }

}
