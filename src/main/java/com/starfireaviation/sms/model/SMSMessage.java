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

package com.starfireaviation.sms.model;

import lombok.Data;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * SMSMessage.
 */
@Data
public class SMSMessage {

    /**
     * UTF-8.
     */
    public static final String UTF8 = "UTF-8";

    /**
     * ToCountry.
     */
    private String toCountry;

    /**
     * ToState.
     */
    private String toState;

    /**
     * SmsMessageSid.
     */
    private String smsMessageSid;

    /**
     * NumMedia.
     */
    private String numMedia;

    /**
     * ToCity.
     */
    private String toCity;

    /**
     * FromZip.
     */
    private String fromZip;

    /**
     * SmsSid.
     */
    private String smsSid;

    /**
     * FromState.
     */
    private String fromState;

    /**
     * SmsStatus.
     */
    private String smsStatus;

    /**
     * FromCity.
     */
    private String fromCity;

    /**
     * Body.
     */
    private String body;

    /**
     * FromCountry.
     */
    private String fromCountry;

    /**
     * To.
     */
    private String destination;

    /**
     * ToZip.
     */
    private String toZip;

    /**
     * NumSegments.
     */
    private String numSegments;

    /**
     * MessageSid.
     */
    private String messageSid;

    /**
     * AccountSid.
     */
    private String accountSid;

    /**
     * From.
     */
    private String from;

    /**
     * ApiVersion.
     */
    private String apiVersion;

    /**
     * Initializes an instance of <code>SMSMessage</code> with the default data.
     *
     * @param message to be parsed
     */
    public SMSMessage(final String message) {
        if (message != null) {
            String[] parts = message.split("&");
            for (String part : parts) {
                try {
                    String[] kvPair = part.split("=");
                    TwilioPart twilioPart = TwilioPart.valueOf(kvPair[0]);
                    switch (twilioPart) {
                        case ToCountry:
                            toCountry = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case ToState:
                            toState = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case SmsMessageSid:
                            smsMessageSid = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case NumMedia:
                            numMedia = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case ToCity:
                            toCity = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case FromZip:
                            fromZip = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case SmsSid:
                            smsSid = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case FromState:
                            fromState = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case SmsStatus:
                            smsStatus = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case FromCity:
                            fromCity = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case Body:
                            body = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case FromCountry:
                            fromCountry = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case To:
                            destination = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case ToZip:
                            toZip = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case NumSegments:
                            numSegments = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case MessageSid:
                            messageSid = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case AccountSid:
                            accountSid = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case From:
                            from = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        case ApiVersion:
                            apiVersion = URLDecoder.decode(kvPair[1], StandardCharsets.UTF_8);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    // Do nothing
                }
            }
        }
    }

}
