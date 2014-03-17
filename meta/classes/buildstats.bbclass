BUILDSTATS_BASE = "${TMPDIR}/buildstats/"
BNFILE = "${BUILDSTATS_BASE}/.buildname"
DEVFILE = "${BUILDSTATS_BASE}/.device"

################################################################################
# Build statistics gathering. 
#
# The CPU and Time gathering/tracking functions and bbevent inspiration
# were written by Christopher Larson and can be seen here: 
# http://kergoth.pastey.net/142813
# 
################################################################################

def get_process_cputime(pid):
    with open("/proc/%d/stat" % pid, "r") as f:
        fields = f.readline().rstrip().split()
    # 13: utime, 14: stime, 15: cutime, 16: cstime
    return sum(int(field) for field in fields[13:16])

def get_cputime():
    with open("/proc/stat", "r") as f:
        fields = f.readline().rstrip().split()[1:]
    return sum(int(field) for field in fields)

def set_bn(e):
    bn = e.getPkgs()[0] + "-" + e.data.getVar('MACHINE', True)
    try:
        os.remove(e.data.getVar('BNFILE', True))
    except:
        pass
    with open(e.data.getVar('BNFILE', True), "w") as f:
        f.write(os.path.join(bn, e.data.getVar('BUILDNAME', True)))

def get_bn(e):
    with open(e.data.getVar('BNFILE', True)) as f:
        bn = f.readline()
    return bn

def set_device(e):
    tmpdir = e.data.getVar('TMPDIR', True)
    try:
        os.remove(e.data.getVar('DEVFILE', True))
    except:
        pass
    ############################################################################
    # We look for the volume TMPDIR lives on. To do all disks would make little 
    # sense and not give us any particularly useful data. In theory we could do
    # something like stick DL_DIR on a different partition and this would
    # throw stats gathering off. The same goes with SSTATE_DIR. However, let's
    # get the basics in here and work on the cornercases later. 
    # A note. /proc/diskstats does not contain info on encryptfs, tmpfs, etc.
    # If we end up hitting one of these fs, we'll just skip diskstats collection.
    ############################################################################
    device=os.stat(tmpdir)
    majordev=os.major(device.st_dev)
    minordev=os.minor(device.st_dev)
    ############################################################################
    # Bug 1700: 
    # Because tmpfs/encryptfs/ramfs etc inserts no entry in /proc/diskstats
    # we set rdev to NoLogicalDevice and search for it later. If we find NLD
    # we do not collect diskstats as the method to collect meaningful statistics
    # for these fs types requires a bit more research. 
    ############################################################################
    rdev="NoLogicalDevice"
    try:
        with open("/proc/diskstats", "r") as f:
            for line in f:
                if majordev == int(line.split()[0]) and minordev == int(line.split()[1]):
                    rdev=line.split()[2]
    except:
        pass
    file = open(e.data.getVar('DEVFILE', True), "w")
    file.write(rdev)
    file.close()
    
def get_device(e):
    file = open(e.data.getVar('DEVFILE', True))
    device = file.readline()
    file.close()
    return device

def get_diskstats(dev):
    import itertools
    ############################################################################
    # For info on what these are, see kernel doc file iostats.txt
    ############################################################################
    DSTAT_KEYS = ['ReadsComp', 'ReadsMerged', 'SectRead', 'TimeReads', 'WritesComp', 'SectWrite', 'TimeWrite', 'IOinProgress', 'TimeIO', 'WTimeIO']  
    try:
        with open("/proc/diskstats", "r") as f:
            for x in f:
                if dev in x:
                    diskstats_val = x.rstrip().split()[4:]
    except IOError as e:
        return
    diskstats = dict(itertools.izip(DSTAT_KEYS, diskstats_val))
    return diskstats

def set_diskdata(var, dev, data):
    data.setVar(var, get_diskstats(dev))

def get_diskdata(var, dev, data):
    olddiskdata = data.getVar(var, False)
    diskdata = {}
    if olddiskdata is None:
        return
    newdiskdata = get_diskstats(dev)
    for key in olddiskdata.iterkeys():
        diskdata["Start"+key] = str(int(olddiskdata[key]))
        diskdata["End"+key] = str(int(newdiskdata[key]))    
    return diskdata
    
def set_timedata(var, data, server_time=None):
    import time
    if server_time:
        time = server_time
    else:
        time = time.time()
    cputime = get_cputime()
    proctime = get_process_cputime(os.getpid())
    data.setVar(var, (time, cputime, proctime))

def get_timedata(var, data, server_time=None):
    import time
    timedata = data.getVar(var, False)
    if timedata is None:
        return
    oldtime, oldcpu, oldproc = timedata
    procdiff = get_process_cputime(os.getpid()) - oldproc
    cpudiff = get_cputime() - oldcpu
    if server_time:
        end_time = server_time
    else:
        end_time = time.time()
    timediff = end_time - oldtime
    if cpudiff > 0:
        cpuperc = float(procdiff) * 100 / cpudiff
    else:
        cpuperc = None
    return timediff, cpuperc
    
def write_task_data(status, logfile, dev, e):
    bn = get_bn(e)
    bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
    taskdir = os.path.join(bsdir, e.data.expand("${PF}"))
    file = open(os.path.join(logfile), "a")
    timedata = get_timedata("__timedata_task", e.data, e.time)
    if timedata:
        elapsedtime, cpu = timedata
        file.write(bb.data.expand("${PF}: %s: Elapsed time: %0.2f seconds \n" %
                                 (e.task, elapsedtime), e.data))
        if cpu:
            file.write("CPU usage: %0.1f%% \n" % cpu)
    ############################################################################
    # Here we gather up disk data. In an effort to avoid lying with stats
    # I do a bare minimum of analysis of collected data.
    # The simple fact is, doing disk io collection on a per process basis
    # without effecting build time would be difficult.
    # For the best information, running things with BB_TOTAL_THREADS = "1"
    # would return accurate per task results.
    ############################################################################
    if dev != "NoLogicalDevice":
        diskdata = get_diskdata("__diskdata_task", dev, e.data)
        if diskdata:
            for key in sorted(diskdata.iterkeys()):
                file.write(key + ": " + diskdata[key] + "\n")
    if status is "passed":
	    file.write("Status: PASSED \n")
    else:
        file.write("Status: FAILED \n")
    file.write("Ended: %0.2f \n" % e.time)
    file.close()

python run_buildstats () {
    import bb.build
    import bb.event
    import bb.data
    import time, subprocess, platform

    if isinstance(e, bb.event.BuildStarted):
        ########################################################################
        # at first pass make the buildstats heriarchy and then
        # set the buildname
        ########################################################################
        try:
            bb.utils.mkdirhier(e.data.getVar('BUILDSTATS_BASE', True))
        except:
            pass
        set_bn(e)
        bn = get_bn(e)
        set_device(e)
        device = get_device(e)
        
        bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
        try:
            bb.utils.mkdirhier(bsdir)
        except:
            pass
        if device != "NoLogicalDevice":
            set_diskdata("__diskdata_build", device, e.data)
        set_timedata("__timedata_build", e.data)
        build_time = os.path.join(bsdir, "build_stats")
        # write start of build into build_time
        file = open(build_time,"a")
        host_info = platform.uname()
        file.write("Host Info: ")
        for x in host_info:
            if x:
                file.write(x + " ")
        file.write("\n")
        file.write("Build Started: %0.2f \n" % time.time())
        file.close()
                
    elif isinstance(e, bb.event.BuildCompleted):
        bn = get_bn(e)
        device = get_device(e)
        bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
        taskdir = os.path.join(bsdir, e.data.expand("${PF}"))
        build_time = os.path.join(bsdir, "build_stats")
        file = open(build_time, "a")
        ########################################################################
        # Write build statistics for the build
        ########################################################################
        timedata = get_timedata("__timedata_build", e.data)
        if timedata:
            time, cpu = timedata
            # write end of build and cpu used into build_time
            file.write("Elapsed time: %0.2f seconds \n" % (time))
            if cpu:
                file.write("CPU usage: %0.1f%% \n" % cpu)
        if device != "NoLogicalDevice":
            diskio = get_diskdata("__diskdata_build", device, e.data)
            if diskio:
                for key in sorted(diskio.iterkeys()):
                    file.write(key + ": " + diskio[key] + "\n")
        file.close()

    if isinstance(e, bb.build.TaskStarted):
        bn = get_bn(e)
        device = get_device(e)
        bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
        taskdir = os.path.join(bsdir, e.data.expand("${PF}"))
        if device != "NoLogicalDevice":
            set_diskdata("__diskdata_task", device, e.data)
        set_timedata("__timedata_task", e.data, e.time)
        try:
            bb.utils.mkdirhier(taskdir)
        except:
            pass
        # write into the task event file the name and start time
        file = open(os.path.join(taskdir, e.task), "a")
        file.write("Event: %s \n" % bb.event.getName(e))
        file.write("Started: %0.2f \n" % e.time)
        file.close()

    elif isinstance(e, bb.build.TaskSucceeded):
        bn = get_bn(e)
        device = get_device(e)
        bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
        taskdir = os.path.join(bsdir, e.data.expand("${PF}"))
        write_task_data("passed", os.path.join(taskdir, e.task), device, e)
        if e.task == "do_rootfs":
            bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
            bs=os.path.join(bsdir, "build_stats")
            file = open(bs,"a") 
            rootfs = e.data.getVar('IMAGE_ROOTFS', True)
            rootfs_size = subprocess.Popen(["du", "-sh", rootfs], stdout=subprocess.PIPE).stdout.read() 
            file.write("Uncompressed Rootfs size: %s" % rootfs_size)
            file.close()

    elif isinstance(e, bb.build.TaskFailed):
        bn = get_bn(e)
        device = get_device(e)
        bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
        taskdir = os.path.join(bsdir, e.data.expand("${PF}"))
        write_task_data("failed", os.path.join(taskdir, e.task), device, e)
        ########################################################################
        # Lets make things easier and tell people where the build failed in 
        # build_status. We do this here because BuildCompleted triggers no 
        # matter what the status of the build actually is
        ########################################################################
        build_status = os.path.join(bsdir, "build_stats")
        file = open(build_status,"a")
        file.write(e.data.expand("Failed at: ${PF} at task: %s \n" % e.task))
        file.close()
}

addhandler run_buildstats
run_buildstats[eventmask] = "bb.event.BuildStarted bb.event.BuildCompleted bb.build.TaskStarted bb.build.TaskSucceeded bb.build.TaskFailed"

