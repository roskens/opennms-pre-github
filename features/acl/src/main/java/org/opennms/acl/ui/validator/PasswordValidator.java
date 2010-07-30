package org.opennms.acl.ui.validator;

import org.opennms.netmgt.model.OnmsUser;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for updates user's password
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */
@Component("passwordValidator")
public class PasswordValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return OnmsUser.class.isAssignableFrom(clazz);
	}

	public void validate(Object command, Errors err) {
		OnmsUser user = (OnmsUser) command;
		updatePassword(err, user);
	}

	private void updatePassword(Errors err, OnmsUser user) {

		ValidationUtils.rejectIfEmptyOrWhitespace(err, "newPassword",
				"password.required.value", "password is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "confirmNewPassword",
				"password.required.value", "password is required.");

		if (!user.getNewPassword().equals(user.getConfirmNewPassword())) {
			err.rejectValue("newPassword", "error.newpassword.match",
					"new password don't match the confirm");
			err.rejectValue("confirmNewPassword", "error.confirmpassword.match",
					"confirm password don't match the new");
		}
		if (!user.getNewPassword().equals("")
				&& user.getNewPassword().length() < 4) {
			err.rejectValue("newPassword", "error.password.length",
					"password too short");
		}
	}
}
