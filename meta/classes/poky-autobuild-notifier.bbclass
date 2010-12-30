#
# Copyright Openedhand Ltd 2008
# Author: Richard Purdie
#

# Designed for use with the Poky autobuilder only and provides custom hooks for 
# certain specific events.

def do_autobuilder_failure_report(event):
    from bb.event import getName
    from bb import data, mkdirhier, build
    import os, glob

    if data.getVar('PN', event.data, True) != "clutter":
        return

    import smtplib
    import email.Message

    version = data.expand("${PN}: ${PV}-${PR}", event.data)

    recipients = ["richard@o-hand.com"]
    COMMASPACE = ', '

    message = email.Message.Message()
    message["To"]      = COMMASPACE.join(recipients)
    message["From"]    = "Poky Autobuilder Failure <poky@o-hand.com>"
    message["Subject"] = "Poky Autobuild Failure Report - " + version

    mesg = "Poky Build Failure for:\n\n"

    for var in ["DISTRO", "MACHINE", "PN", "PV", "PR"]:
        mesg += var + ": " + data.getVar(var, event.data, True) + "\n"

    mesg += "\nLog of the failure follows:\n\n"

    log_file = glob.glob("%s/log.%s.*" % (data.getVar('T', event.data, True), event.task))
    if len(log_file) != 0:
        mesg += "".join(open(log_file[0], 'r').readlines())

    message.set_payload(mesg)

    mailServer = smtplib.SMTP("pug.o-hand.com")
    mailServer.sendmail(message["From"], recipients, message.as_string())
    mailServer.quit()

# we want to be an event handler
addhandler poky_autobuilder_notifier_eventhandler
python poky_autobuilder_notifier_eventhandler() {
    from bb import note, error, data
    from bb.event import getName

    if e.data is None:
        return

    name = getName(e)

    if name == "TaskFailed":
        do_autobuilder_failure_report(e)

    return
}
