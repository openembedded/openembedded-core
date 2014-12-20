#
# Collects debug information in order to create error report files.
#
# Copyright (C) 2013 Intel Corporation
# Author: Andreea Brandusa Proca <andreea.b.proca@intel.com>
#
# Licensed under the MIT license, see COPYING.MIT for details

ERR_REPORT_DIR ?= "${LOG_DIR}/error-report"

def errorreport_getdata(e):
    logpath = e.data.getVar('ERR_REPORT_DIR', True)
    datafile = os.path.join(logpath, "error-report.txt")
    with open(datafile) as f:
        data = f.read()
    return data

def errorreport_savedata(e, newdata, file):
    import json
    logpath = e.data.getVar('ERR_REPORT_DIR', True)
    bb.utils.mkdirhier(logpath)
    datafile = os.path.join(logpath, file)
    with open(datafile, "w") as f:
        json.dump(newdata, f, indent=4, sort_keys=True)
    return datafile

python errorreport_handler () {
        import json

        if isinstance(e, bb.event.BuildStarted):
            data = {}
            machine = e.data.getVar("MACHINE")
            data['machine'] = machine
            data['build_sys'] = e.data.getVar("BUILD_SYS", True)
            data['nativelsb'] = e.data.getVar("NATIVELSBSTRING")
            data['distro'] = e.data.getVar("DISTRO")
            data['target_sys'] = e.data.getVar("TARGET_SYS", True)
            data['failures'] = []
            data['component'] = e.getPkgs()[0]
            data['branch_commit'] = base_detect_branch(e.data) + ": " + base_detect_revision(e.data)
            errorreport_savedata(e, data, "error-report.txt")

        elif isinstance(e, bb.build.TaskFailed):
            task = e.task
            taskdata={}
            log = e.data.getVar('BB_LOGFILE', True)
            taskdata['package'] = e.data.expand("${PF}")
            taskdata['task'] = task
            if log:
                logFile = open(log, 'r')
                taskdata['log'] = logFile.read()
                logFile.close()
            else:
                taskdata['log'] = "No Log"
            jsondata = json.loads(errorreport_getdata(e))
            jsondata['failures'].append(taskdata)
            errorreport_savedata(e, jsondata, "error-report.txt")

        elif isinstance(e, bb.event.BuildCompleted):
            jsondata = json.loads(errorreport_getdata(e))
            failures = jsondata['failures']
            if(len(failures) > 0):
                filename = "error_report_" + e.data.getVar("BUILDNAME")+".txt"
                datafile = errorreport_savedata(e, jsondata, filename)
                bb.note("The errors for this build are stored in %s\nYou can send the errors to an upstream server by running:\n  send-error-report %s [server]" % (datafile, datafile))
                bb.note("The contents of these logs will be posted in public if you use the above command with the default server. If you need to do so, please ensure you remove any identifying or proprietary information before sending.")
}

addhandler errorreport_handler
errorreport_handler[eventmask] = "bb.event.BuildStarted bb.event.BuildCompleted bb.build.TaskFailed"
