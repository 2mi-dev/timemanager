package navy.otter.web.rest.error;

public class EmailAlreadyUsedException extends BadRequestAlertException {

  public EmailAlreadyUsedException() {
    super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email address already in use", "userManagement",
      "emailexists");
  }
}

