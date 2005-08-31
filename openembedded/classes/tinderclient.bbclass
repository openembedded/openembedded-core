def tinder_tz_offset(off):
    # get the offset.n minutes Either it is a number like
    # +200 or -300
    try:
        return int(off)
    except ValueError:
        if off == "Europe/Berlin":
            return 120
        else:
            return 0

def tinder_tinder_time(offset):
    import datetime
    td   = datetime.timedelta(minutes=tinder_tz_offset(offset))
    time = datetime.datetime.utcnow() + td
    return time.strftime('%m/%d/%Y %H:%M:%S')

def tinder_tinder_start(date,offset):
    import datetime, time
    td   = datetime.timedelta(minutes=tinder_tz_offset(offset))
    ti   = time.strptime(date, "%m/%d/%Y %H:%M:%S")
    time = datetime.datetime(*ti[0:7])+td
    return time.strftime('%m/%d/%Y %H:%M:%S')

def tinder_send_email(da, header, log):
    import smtplib
    from bb import data
    from email.MIMEText import MIMEText
    msg = MIMEText(header +'\n' + log)
    msg['Subject'] = data.getVar('TINDER_SUBJECT',da, True) or "Tinder-Client build log"
    msg['To']      = data.getVar('TINDER_MAILTO' ,da, True)
    msg['From']    = data.getVar('TINDER_FROM',   da, True)


    s = smtplib.SMTP()
    s.connect()
    s.sendmail(data.getVar('TINDER_FROM', da, True), [data.getVar('TINDER_MAILTO', da, True)], msg.as_string())
    s.close()

def tinder_send_http(da, header, log):
    from bb import data
    import httplib, urllib
    cont = "\n%s\n%s" % ( header, log)
    headers = {"Content-type": "multipart/form-data" }

    conn = httplib.HTTPConnection(data.getVar('TINDER_HOST',da, True))
    conn.request("POST", data.getVar('TINDER_URL',da,True), cont, headers)
    conn.close() 


# Prepare tinderbox mail header
def tinder_prepare_mail_header(da, status):
    from bb import data

    str  = "tinderbox: administrator: %s\n" % data.getVar('TINDER_ADMIN', da, True)
    str += "tinderbox: starttime: %s\n"     % tinder_tinder_start(data.getVar('TINDER_START', da, True) or data.getVar('BUILDSTART', da, True), data.getVar('TINDER_TZ', da, True))
    str += "tinderbox: buildname: %s\n"     % data.getVar('TINDER_BUILD', da, True)
    str += "tinderbox: errorparser: %s\n"   % data.getVar('TINDER_ERROR', da, True)
    str += "tinderbox: status: %s\n"        % status
    str += "tinderbox: timenow: %s\n"       % tinder_tinder_time(data.getVar('TINDER_TZ', da, True))
    str += "tinderbox: tree: %s\n"          % data.getVar('TINDER_TREE', da, True)
    str += "tinderbox: buildfamily: %s\n"   % "unix"
    str += "tinderbox: END"

    return str

def tinder_do_tinder_report(event):
    """
    Report to the tinderbox. Either we will report every step
    (depending on TINDER_VERBOSE_REPORT) at the end we will send the
    tinderclient.log
    """
    from bb.event import getName
    from bb import data, mkdirhier
    import os, glob

    # variables
    name = getName(event)
    log  = ""
    header = ""
    verbose = data.getVar('TINDER_VERBOSE_REPORT', event.data, True) == "1"

    # Check what we need to do Build* shows we start or are done
    if name == "BuildStarted":
        header = tinder_prepare_mail_header(event.data, 'building')
        # generate
        for var in os.environ:
            log += "%s=%s\n" % (var, os.environ[var])

        mkdirhier(data.getVar('TMPDIR', event.data, True))
        file = open(data.getVar('TINDER_LOG', event.data, True), 'w')
        file.write(log)

        if not verbose:
            header = ""

    if name == "PkgFailed" or name == "BuildCompleted":
        status = 'build_failed'
        if name == "BuildCompleted":
            status = "success"
        header = tinder_prepare_mail_header(event.data, status)
        # append the log
        log_file = data.getVar('TINDER_LOG', event.data, True)
        file     = open(log_file, 'r')
        for line in file.readlines():
            log += line

    if verbose and name == "TaskStarted":
        header = tinder_prepare_mail_header(event.data, 'building')
        log    = "Task %s started" % event.task

    if verbose and name == "PkgStarted":
        header = tinder_prepare_mail_header(event.data, 'building')
        log    = "Package %s started" % data.getVar('P', event.data, True)

    if verbose and name == "PkgSucceeded":
        header = tinder_prepare_mail_header(event.data, 'building')
        log    = "Package %s done" % data.getVar('P', event.data, True)

    # Append the Task Log
    if name == "TaskSucceeded" or name == "TaskFailed":
        log_file = glob.glob("%s/log.%s.*" % (data.getVar('T', event.data, True), event.task))

        if len(log_file) != 0:
            to_file  = data.getVar('TINDER_LOG', event.data, True)
            log_txt  = open(log_file[0], 'r').readlines()
            to_file  = open(to_file, 'a')

            to_file.writelines(log_txt)

            # append to the log
            if verbose:
                header = tinder_prepare_mail_header(event.data, 'building')
                for line in log_txt:
                    log += line

    # now mail the log
    if len(log) == 0 or len(header) == 0:
        return

    log_post_method = tinder_send_email
    if data.getVar('TINDER_SENDLOG', event.data, True) == "http":
        log_post_method = tinder_send_http

    log_post_method(event.data, header, log)


addhandler tinderclient_eventhandler
python tinderclient_eventhandler() {
    from bb import note, error, data
    from bb.event import NotHandled

    do_tinder_report = data.getVar('TINDER_REPORT', e.data, True)
    if do_tinder_report and do_tinder_report == "1":
        tinder_do_tinder_report(e)

    return NotHandled
}
