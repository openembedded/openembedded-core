BUILDSTATS_BASE = ${TMPDIR}/buildstats/
BNFILE = ${BUILDSTATS_BASE}/.buildname

################################################################################
# Build statistics gathering. 
#
# The CPU and Time gathering/tracking functions and bbevent inspiration
# were written by Christopher Larson and can be seen here: 
# http://kergoth.pastey.net/142813
# 
################################################################################

def get_process_cputime(pid):
    fields = open("/proc/%d/stat" % pid, "r").readline().rstrip().split()
    # 13: utime, 14: stime, 15: cutime, 16: cstime
    return sum(int(field) for field in fields[13:16])

def get_cputime():
    fields = open("/proc/stat", "r").readline().rstrip().split()[1:]
    return sum(int(field) for field in fields)

def set_timedata(var, data):
    import time

    time = time.time()
    cputime = get_cputime()
    proctime = get_process_cputime(os.getpid())
    data.setVar(var, (time, cputime, proctime))

def get_timedata(var, data):
    import time
    timedata = data.getVar(var, False)
    if timedata is None:
        return
    oldtime, oldcpu, oldproc = timedata
    procdiff = get_process_cputime(os.getpid()) - oldproc
    cpudiff = get_cputime() - oldcpu
    timediff = time.time() - oldtime
    if cpudiff > 0:
        cpuperc = float(procdiff) * 100 / cpudiff
    else:
        cpuperc = None
    return timediff, cpuperc
    
##############################################
# We need to set the buildname to a file since
# BUILDNAME changes throughout a build
##############################################

def set_bn(e):
    bn = e.getPkgs()[0] + "-" + bb.data.getVar('MACHINE',e.data, True)
    try:
        os.remove(bb.data.getVar('BNFILE',e.data, True))
    except:
        pass
    file = open(bb.data.getVar('BNFILE',e.data, True), "w")
    file.write(os.path.join(bn, bb.data.getVar('BUILDNAME', e.data, True)))
    file.close()

def get_bn(e):
    file = open(bb.data.getVar('BNFILE',e.data, True))
    bn = file.readline()
    file.close()
    return bn

python run_buildstats () {
    import bb.build
    import bb.event
    import bb.data
    import time, subprocess

    if isinstance(e, bb.event.BuildStarted):
        ##############################################
        # at first pass make the buildstats heriarchy and then
        # set the buildname
        ##############################################
        try:
            bb.mkdirhier(bb.data.getVar('BUILDSTATS_BASE', e.data, True))
        except:
            pass
        set_bn(e)
        bn = get_bn(e)
        bb.warn(bn)
        bsdir = os.path.join(bb.data.getVar('BUILDSTATS_BASE', e.data, True), bn)
        try:
            bb.mkdirhier(bsdir)
        except:
            pass
        set_timedata("__timedata_build", e.data)
        build_time = os.path.join(bsdir, "build_stats")
        # write start of build into build_time
        file = open(build_time,"a")
        # We do this here because subprocess within BuildStarted is messy
        #host_info = subprocess.Popen(["uname", "-a"], stdout=subprocess.PIPE).stdout.read() 
        #file.write("Host Info: %s" % host_info)
        file.write("Build Started: %0.2f \n" % time.time())
        file.close()
                
    elif isinstance(e, bb.event.BuildCompleted):
        bn=get_bn(e)
        timedata = get_timedata("__timedata_build", e.data)
        if not timedata:
            return
        time, cpu = timedata
        bsdir = os.path.join(bb.data.getVar('BUILDSTATS_BASE', e.data, True), bn)
        build_time = os.path.join(bsdir, "build_stats")
        # write end of build and cpu used into build_time
        file = open(build_time, "a")
        file.write("Elapsed time: %0.2f seconds \n" % (time))
        if cpu:
            file.write("CPU usage: %0.1f%% \n" % cpu)
        file.close()


    if isinstance(e, bb.build.TaskStarted):
        bn=get_bn(e)
        set_timedata("__timedata_task", e.data)
        
        bsdir = os.path.join(bb.data.getVar('BUILDSTATS_BASE', e.data, True), bn)
        taskdir = os.path.join(bsdir, bb.data.expand("${PF}", e.data))
        try:
            bb.mkdirhier(taskdir)
        except:
            pass
        # write into the task event file the name and start time
        file = open(os.path.join(taskdir, e.task), "a")
        file.write("Event: %s \n" % bb.event.getName(e))
        file.write("Started: %0.2f \n" % time.time())
        file.close()

    elif isinstance(e, bb.build.TaskSucceeded):
        bn=get_bn(e)
        timedata = get_timedata("__timedata_task", e.data)
        if not timedata:
            return
        elapsedtime, cpu = timedata
        bsdir = os.path.join(bb.data.getVar('BUILDSTATS_BASE', e.data, True), bn)
        taskdir = os.path.join(bsdir, bb.data.expand("${PF}", e.data))
        file = open(os.path.join(taskdir, e.task), "a")
        file.write(bb.data.expand("${PF}: %s: Elapsed time: %0.2f seconds \n" %
                               (e.task, elapsedtime), e.data))
        file.write("Ended: %0.2f \n" % time.time())
        if cpu:
            file.write("CPU usage: %0.1f%% \n" % cpu)
    	
    	file.write("Status: PASSED")
        file.close()

        ##############################################
        # Alot of metric gathering occurs here. 
        # Reminder: I stripped out some in process stuff here
        ##############################################

        if e.task == "do_rootfs":
            bs=os.path.join(bsdir, "build_stats")
            file = open(bs,"a") 
            rootfs = bb.data.getVar('IMAGE_ROOTFS', e.data, True)
            rootfs_size = subprocess.Popen(["du", "-sh", rootfs], stdout=subprocess.PIPE).stdout.read() 
            file.write("Uncompressed Rootfs size: %s" % rootfs_size)
            file.close()

    elif isinstance(e, bb.build.TaskFailed):
        bn=get_bn(e)
        timedata = get_timedata("__timedata_task", e.data)
        if not timedata:
            return
        time, cpu = timedata
        bsdir = os.path.join(bb.data.getVar('BUILDSTATS_BASE', e.data, True), bn)
        taskdir = os.path.join(bsdir, bb.data.expand("${PF}", e.data))
        ##############################################
        # If the task fails dump the regular data. 
        # fgrep -R "FAILED" <bsdir> 
        # will grep all the events that failed. 
        ##############################################
        file = open(os.path.join(taskdir, e.task), "a")
        file.write(bb.data.expand("${PF}: %s: Elapsed time: %0.2f seconds \n" %
                               (e.task, time), e.data))
        if cpu:
            file.write("CPU usage: %0.1f%% \n" % cpu)
        file.write("Status: FAILED")
        file.close()
        ##############################################
        # Lets make things easier and tell people where the build failed in build_status
        # We do this here because BuildCompleted triggers no matter what the status of the
        # build actually is
        ##############################################
        build_status = os.path.join(bsdir, "build_stats")
        file = open(build_status,"a")
        file.write(bb.data.expand("Failed at: ${PF} at task: %s \n", e.task))
        file.close()

}

addhandler run_buildstats

