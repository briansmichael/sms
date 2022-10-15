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

package com.starfireaviation.sms.service;

import com.starfireaviation.model.Event;
import com.starfireaviation.model.EventType;
import com.starfireaviation.model.Message;
import com.starfireaviation.model.NotificationType;
import com.starfireaviation.model.Question;
import com.starfireaviation.model.Quiz;
import com.starfireaviation.model.User;
import com.starfireaviation.sms.config.ApplicationProperties;
import com.starfireaviation.sms.config.CommonConstants;
import com.starfireaviation.sms.exception.InvalidPayloadException;
import com.starfireaviation.sms.model.SMSMessage;
import com.starfireaviation.sms.util.TemplateUtil;
import com.starfireaviation.sms.validation.ResponseValidator;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MessageService.
 */
@Slf4j
public class MessageService {

    /**
     * TN_PATTERN.
     */
    private static final Pattern TN_PATTERN = Pattern.compile(".*\\+1(\\d{10}).*");

    /**
     * TEMPLATE_LOCATION.
     */
    private static final String TEMPLATE_LOCATION = "/templates";

    /**
     * ApplicationProperties.
     */
    private final ApplicationProperties applicationProperties;

    /**
     * FreeMarker Configuration.
     */
    private final Configuration freemarkerConfig;

    /**
     * MessageService.
     *
     * @param aProps ApplicationProperties
     * @param config Configuration
     */
    public MessageService(final ApplicationProperties aProps,
                      final Configuration config) {
        applicationProperties = aProps;
        freemarkerConfig = config;
    }

    /**
     * Sends a message for user deletion.
     *
     * @param message Message
     */
    public void sendUserDeleteMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            send(
                    applicationProperties.getFromAddress(),
                    user.getSms(),
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("user_delete.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for quiz completion.
     *
     * @param message Message
     */
    public void sendQuizCompleteMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            send(
                    applicationProperties.getFromAddress(),
                    user.getSms(),
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("quiz_complete.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message to RSVP for an upcoming event.
     *
     * @param message Message
     */
    public void sendEventRSVPMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Event event = getEvent(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(
                        applicationProperties.getFromAddress(),
                        user.getSms(),
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_rsvp.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for an upcoming event.
     *
     * @param message Message
     */
    public void sendEventUpcomingMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Event event = getEvent(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(
                        applicationProperties.getFromAddress(),
                        user.getSms(),
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_upcoming.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message to a user that an event has started.
     *
     * @param message Message
     */
    public void sendEventStartMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Event event = getEvent(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(
                        applicationProperties.getFromAddress(),
                        user.getSms(),
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_start.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message that a question has been asked.
     *
     * @param message Message
     */
    public void sendQuestionAskedMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Question question = getQuestion(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);

            send(
                    applicationProperties.getFromAddress(),
                    user.getSms(),
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("question.ftl"),
                            TemplateUtil.getModel(user, null, question, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for registering for an upcoming event.
     *
     * @param message Message
     */
    public void sendEventRegisterMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(
                        applicationProperties.getFromAddress(),
                        user.getSms(),
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_register.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for unregistering from an upcoming event.
     *
     * @param message Message
     */
    public void sendEventUnregisterMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(
                        applicationProperties.getFromAddress(),
                        user.getSms(),
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_unregister.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for user settings verified.
     *
     * @param message Message
     */
    public void sendUserSettingsVerifiedMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            send(
                    applicationProperties.getFromAddress(),
                    user.getSms(),
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("user_settings_verified.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for user settings changed.
     *
     * @param message Message
     */
    public void sendUserSettingsChangeMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            send(
                    applicationProperties.getFromAddress(),
                    user.getSms(),
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("user_verify_settings.ftl"),
                            TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Resends a message for user settings changed.
     *
     * @param message Message
     * @param response Response
     * @param originalMessage Original Message
     */
    public void resendUserSettingsChangeMsg(
            final Message message,
            final String response,
            final String originalMessage) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            Map<String, Object> model = TemplateUtil.getModel(user, null, null, applicationProperties);
            model.put("response", response);
            model.put("original_message", originalMessage);
            send(
                    applicationProperties.getFromAddress(),
                    user.getSms(),
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("resend_header.ftl"),
                            model));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a password reset message.
     *
     * @param message Message
     */
    public void sendPasswordResetMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            Map<String, Object> model = TemplateUtil.getModel(user, null, null, applicationProperties);
            model.put("code", user.getCode());
            send(
                    applicationProperties.getFromAddress(),
                    user.getSms(),
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            freemarkerConfig.getTemplate("password_reset.ftl"),
                            model));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a last minute message to register/RSVP for an upcoming event.
     *
     * @param message Message
     */
    public void sendEventLastMinRegistrationMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Event event = getEvent(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(
                        applicationProperties.getFromAddress(),
                        user.getSms(),
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate("gs_event_last_min_registration.ftl"),
                                TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message to a user that an event has completed.
     *
     * @param message Message
     */
    public void sendEventCompletedMsg(final Message message) {
    }

    /**
     * Receives a message and returns response.
     *
     * @param message received
     * @return response
     */
    public String receiveMessage(final SMSMessage message) {
        if (!applicationProperties.isEnabled()) {
            return null;
        }
        String response = null;
        final String msg = String.format("receiveMessage() message received was [%s]", message);
        log.info(msg);
        try {
            ResponseValidator.validate(message.getBody());
        } catch (InvalidPayloadException e) {
            return response;
        }

        processUserResponse(stripCountryCode(message.getFrom()), message.getBody(), NotificationType.SMS);
        return response;
    }

    /**
     * Sends an SMS.
     *
     * @param fromAddress from address
     * @param toAddress   to address
     * @param body        body
     */
    private void send(
            final String fromAddress,
            final String toAddress,
            final String body) {
        final String msg = String.format(
                "Sending... fromAddress [%s]; toAddress [%s]; body [%s]",
                fromAddress,
                toAddress,
                body);
        log.info(msg);
        Twilio.init(applicationProperties.getAccountSid(), applicationProperties.getAuthId());
        com.twilio.rest.api.v2010.account.Message
                .creator(new PhoneNumber(toAddress), new PhoneNumber(fromAddress), body).create();
    }

    /**
     * Process user response.
     *
     * @param message          received from user
     * @param to               user
     * @param notificationType NotificationType
     */
    protected void processUserResponse(
            final String to,
            final String message,
            final NotificationType notificationType) {
        // TODO
    }

    /**
     * Strips the country code from a phone number.
     *
     * @param from number
     * @return phone number minus country code
     */
    protected static String stripCountryCode(final String from) {
        if (from == null || from.length() == CommonConstants.TEN) {
            return from;
        }
        String response = from;
        Matcher matcher = TN_PATTERN.matcher(from);
        if (matcher.find()) {
            response = matcher.group(1);
        }
        return response;
    }

    private Event getEvent(final Message message) {
        return null;
    }

    private User getUser(final Message message) {
        return null;
    }

    private Question getQuestion(final Message message) {
        return null;
    }

    private Quiz getQuiz(final Message message) {
        return null;
    }
}
