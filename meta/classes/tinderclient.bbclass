def tinder_http_post(server, selector, content_type, body):
    import httplib
    # now post it
    for i in range(0,5):
       try:
           h = httplib.HTTP(server)
           h.putrequest('POST', selector)
           h.putheader('content-type', content_type)
           h.putheader('content-length', str(len(body)))
           h.endheaders()
           h.send(body)
           errcode, errmsg, headers = h.getreply()
           #print errcode, errmsg, headers
           return (errcode,errmsg, headers, h.file)
       except:
           # try again
           pass

def tinder_form_data(bound, dict, log):
    output = []
  #br
    # for each key in the dictionary
    for name in dict:
        output.append( "--" + bound )
        output.append( 'Content-Disposition: form-data; name="%s"' % name )
        output.append( "" )
        output.append( dict[name] )
    if log:
        output.append( "--" + bound )
        output.append( 'Content-Disposition: form-data; name="log"; filename="log.txt"' )
        output.append( '' )
        output.append( log )
    output.append( '--' + bound + '--' )
    output.append( '' )

    return "\r\n".join(output)

def tinder_time_string():
    """
    Return the time as GMT
    """
    return ""

def tinder_format_http_post(d,status,log):
    """
    Format the Tinderbox HTTP post with the data needed
    for the tinderbox to be happy.
    """

    from bb import data, build
    import os,random

    # the variables we will need to send on this form post
    variables =  {
        "tree"         : data.getVar('TINDER_TREE',    d, True),
        "machine_name" : data.getVar('TINDER_MACHINE', d, True),
        "os"           : os.uname()[0],
        "os_version"   : os.uname()[2],
        "compiler"     : "gcc",
        "clobber"      : data.getVar('TINDER_CLOBBER', d, True)
    }

    # optionally add the status
    if status:
        variables["status"] = str(status)

    # try to load the machine id
    # we only need on build_status.pl but sending it
    # always does not hurt
    try:
        f = file(data.getVar('TMPDIR',d,True)+'/tinder-machine.id', 'r')
        id = f.read()
        variables['machine_id'] = id
    except:
        pass

    # the boundary we will need
    boundary = "----------------------------------%d" % int(random.random()*1000000000000)

    # now format the body
    body = tinder_form_data( boundary, variables, log )

    return ("multipart/form-data; boundary=%s" % boundary),body


def tinder_build_start(d):
    """
    Inform the tinderbox that a build is starting. We do this
    by posting our name and tree to the build_start.pl script
    on the server.
    """
    from bb import data

    # get the body and type
    content_type, body = tinder_format_http_post(d,None,None)
    server = data.getVar('TINDER_HOST', d, True )
    url    = data.getVar('TINDER_URL',  d, True )

    selector = url + "/xml/build_start.pl"

    #print "selector %s and url %s" % (selector, url)

    # now post it
    errcode, errmsg, headers, h_file = tinder_http_post(server,selector,content_type, body)
    #print errcode, errmsg, headers
    report = h_file.read()

    # now let us find the machine id that was assigned to us
    search = "<machine id='"
    report = report[report.find(search)+len(search):]
    report = report[0:report.find("'")]

    import bb
    bb.note("Machine ID assigned by tinderbox: %s" % report )

    # now we will need to save the machine number
    # we will override any previous numbers
    f = file(data.getVar('TMPDIR', d, True)+"/tinder-machine.id", 'w')
    f.write(report)


def tinder_send_http(d, status, log):
    """
    Send this log as build status
    """
    from bb import data


    # get the body and type
    content_type, body = tinder_format_http_post(d,status,log)
    server = data.getVar('TINDER_HOST', d, True )
    url    = data.getVar('TINDER_URL',  d, True )

    selector = url + "/xml/build_status.pl"

    # now post it
    errcode, errmsg, headers, h_file = tinder_http_post(server,selector,content_type, body)
    #print errcode, errmsg, headers
    #print h.file.read()


def tinder_print_info(d):
    """
    Print the TinderBox Info
        Including informations of the BaseSystem and the Tree
        we use.
    """

    from   bb import data
    import os
    # get the local vars

    time    = tinder_time_string()
    ops     = os.uname()[0]
    version = os.uname()[2]
    url     = data.getVar( 'TINDER_URL' , d, True )
    tree    = data.getVar( 'TINDER_TREE', d, True )
    branch  = data.getVar( 'TINDER_BRANCH', d, True )
    srcdate = data.getVar( 'SRCDATE', d, True )
    machine = data.getVar( 'MACHINE', d, True )
    distro  = data.getVar( 'DISTRO',  d, True )
    bbfiles = data.getVar( 'BBFILES', d, True )
    tarch   = data.getVar( 'TARGET_ARCH', d, True )
    fpu     = data.getVar( 'TARGET_FPU', d, True )
    oerev   = data.getVar( 'OE_REVISION', d, True ) or "unknown"

    # there is a bug with tipple quoted strings
    # i will work around but will fix the original
    # bug as well
    output = []
    output.append("== Tinderbox Info" )
    output.append("Time: %(time)s" )
    output.append("OS: %(ops)s" )
    output.append("%(version)s" )
    output.append("Compiler: gcc" )
    output.append("Tinderbox Client: 0.1" )
    output.append("Tinderbox Client Last Modified: yesterday" )
    output.append("Tinderbox Protocol: 0.1" )
    output.append("URL: %(url)s" )
    output.append("Tree: %(tree)s" )
    output.append("Config:" )
    output.append("branch = '%(branch)s'" )
    output.append("TARGET_ARCH = '%(tarch)s'" )
    output.append("TARGET_FPU = '%(fpu)s'" )
    output.append("SRCDATE = '%(srcdate)s'" )
    output.append("MACHINE = '%(machine)s'" )
    output.append("DISTRO = '%(distro)s'" )
    output.append("BBFILES = '%(bbfiles)s'" )
    output.append("OEREV = '%(oerev)s'" )
    output.append("== End Tinderbox Client Info" )

    # now create the real output
    return "\n".join(output) % vars()


def tinder_print_env():
    """
    Print the environment variables of this build
    """
    from bb import data
    import os

    time_start = tinder_time_string()
    time_end   = tinder_time_string()

    # build the environment
    env = ""
    for var in os.environ:
        env += "%s=%s\n" % (var, os.environ[var])

    output = []
    output.append( "---> TINDERBOX RUNNING env %(time_start)s" )
    output.append( env )
    output.append( "<--- TINDERBOX FINISHED (SUCCESS) %(time_end)s" )

    return "\n".join(output) % vars()

def tinder_tinder_start(d, event):
    """
    PRINT the configuration of this build
    """

    time_start = tinder_time_string()
    config = tinder_print_info(d)
    #env    = tinder_print_env()
    time_end   = tinder_time_string()
    packages = " ".join( event.getPkgs() ) 

    output = []
    output.append( "---> TINDERBOX PRINTING CONFIGURATION %(time_start)s" )
    output.append( config )
    #output.append( env    )
    output.append( "<--- TINDERBOX FINISHED PRINTING CONFIGURATION %(time_end)s" )
    output.append( "---> TINDERBOX BUILDING '%(packages)s'" )
    output.append( "<--- TINDERBOX STARTING BUILD NOW" )

    output.append( "" ) 
 
    return "\n".join(output) % vars()

def tinder_do_tinder_report(event):
    """
    Report to the tinderbox:
        On the BuildStart we will inform the box directly
        On the other events we will write to the TINDER_LOG and
        when the Task is finished we will send the report.

    The above is not yet fully implemented. Currently we send
    information immediately. The caching/queuing needs to be
    implemented. Also sending more or less information is not
    implemented yet.
    """
    from bb.event import getName
    from bb import data, mkdirhier, build
    import os, glob

    # variables
    name = getName(event)
    log  = ""
    status = 1
    #print asd 
    # Check what we need to do Build* shows we start or are done
    if name == "BuildStarted":
        tinder_build_start(event.data)
        log = tinder_tinder_start(event.data,event)

        try:
            # truncate the tinder log file
            f = file(data.getVar('TINDER_LOG', event.data, True), 'rw+')
            f.truncate(0)
            f.close()
        except IOError:
            pass

    # Append the Task-Log (compile,configure...) to the log file
    # we will send to the server
    if name == "TaskSucceeded" or name == "TaskFailed":
        log_file = glob.glob("%s/log.%s.*" % (data.getVar('T', event.data, True), event.task))

        if len(log_file) != 0:
            to_file  = data.getVar('TINDER_LOG', event.data, True)
            log     += "".join(open(log_file[0], 'r').readlines())

    # set the right 'HEADER'/Summary for the TinderBox
    if name == "TaskStarted":
        log += "---> TINDERBOX Task %s started\n" % event.task
    elif name == "TaskSucceeded":
        log += "<--- TINDERBOX Task %s done (SUCCESS)\n" % event.task
    elif name == "TaskFailed":
        log += "<--- TINDERBOX Task %s failed (FAILURE)\n" % event.task
    elif name == "PkgStarted":
        log += "---> TINDERBOX Package %s started\n" % data.getVar('P', event.data, True)
    elif name == "PkgSucceeded":
        log += "<--- TINDERBOX Package %s done (SUCCESS)\n" % data.getVar('P', event.data, True)
    elif name == "PkgFailed":
        build.exec_task('do_clean', event.data)
        log += "<--- TINDERBOX Package %s failed (FAILURE)\n" % data.getVar('P', event.data, True)
        status = 200
    elif name == "BuildCompleted":
        log += "Build Completed\n"
        status = 100
    elif name == "MultipleProviders":
        log += "---> TINDERBOX Multiple Providers\n"
        log += "multiple providers are available (%s);\n" % ", ".join(event.getCandidates())
        log += "consider defining PREFERRED_PROVIDER_%s\n" % event.getItem()
        log += "is runtime: %d\n" % event.isRuntime()
        log += "<--- TINDERBOX Multiple Providers\n"
    elif name == "NoProvider":
        log += "Error: No Provider for: %s\n" % event.getItem()
        log += "Error:Was Runtime: %d\n" % event.isRuntime()
        status = 200

    # now post the log
    if len(log) == 0:
        return

    # for now we will use the http post method as it is the only one
    log_post_method = tinder_send_http
    log_post_method(event.data, status, log)


# we want to be an event handler
addhandler tinderclient_eventhandler
python tinderclient_eventhandler() {
    from bb import note, error, data
    from bb.event import NotHandled
    do_tinder_report = data.getVar('TINDER_REPORT', e.data, True)
    if do_tinder_report and do_tinder_report == "1":
        tinder_do_tinder_report(e)

    return NotHandled
}
