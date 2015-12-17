BUILDSTATS_BASE = "${TMPDIR}/buildstats/"
BUILDSTATS_BNFILE = "${BUILDSTATS_BASE}/.buildname"

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
        os.remove(e.data.getVar('BUILDSTATS_BNFILE', True))
    except:
        pass
    with open(e.data.getVar('BUILDSTATS_BNFILE', True), "w") as f:
        f.write(os.path.join(bn, e.data.getVar('BUILDNAME', True)))

def get_bn(e):
    with open(e.data.getVar('BUILDSTATS_BNFILE', True)) as f:
        bn = f.readline()
    return bn

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

def write_task_data(status, logfile, e):
    bn = get_bn(e)
    bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
    with open(os.path.join(logfile), "a") as f:
        timedata = get_timedata("__timedata_task", e.data, e.time)
        if timedata:
            elapsedtime, cpu = timedata
            f.write(bb.data.expand("${PF}: %s: Elapsed time: %0.2f seconds \n" %
                                    (e.task, elapsedtime), e.data))
            if cpu:
                f.write("CPU usage: %0.1f%% \n" % cpu)
        if status is "passed":
            f.write("Status: PASSED \n")
        else:
            f.write("Status: FAILED \n")
        f.write("Ended: %0.2f \n" % e.time)

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
        bb.utils.mkdirhier(e.data.getVar('BUILDSTATS_BASE', True))
        set_bn(e)
        bn = get_bn(e)

        bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
        bb.utils.mkdirhier(bsdir)
        set_timedata("__timedata_build", e.data)
        build_time = os.path.join(bsdir, "build_stats")
        # write start of build into build_time
        with open(build_time, "a") as f:
            host_info = platform.uname()
            f.write("Host Info: ")
            for x in host_info:
                if x:
                    f.write(x + " ")
            f.write("\n")
            f.write("Build Started: %0.2f \n" % time.time())

    elif isinstance(e, bb.event.BuildCompleted):
        bn = get_bn(e)
        bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
        build_time = os.path.join(bsdir, "build_stats")
        with open(build_time, "a") as f:
            ########################################################################
            # Write build statistics for the build
            ########################################################################
            timedata = get_timedata("__timedata_build", e.data)
            if timedata:
                time, cpu = timedata
                # write end of build and cpu used into build_time
                f.write("Elapsed time: %0.2f seconds \n" % (time))
                if cpu:
                    f.write("CPU usage: %0.1f%% \n" % cpu)

    if isinstance(e, bb.build.TaskStarted):
        bn = get_bn(e)
        bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
        taskdir = os.path.join(bsdir, e.data.getVar('PF', True))
        set_timedata("__timedata_task", e.data, e.time)
        bb.utils.mkdirhier(taskdir)
        # write into the task event file the name and start time
        with open(os.path.join(taskdir, e.task), "a") as f:
            f.write("Event: %s \n" % bb.event.getName(e))
            f.write("Started: %0.2f \n" % e.time)

    elif isinstance(e, bb.build.TaskSucceeded):
        bn = get_bn(e)
        bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
        taskdir = os.path.join(bsdir, e.data.getVar('PF', True))
        write_task_data("passed", os.path.join(taskdir, e.task), e)
        if e.task == "do_rootfs":
            bs = os.path.join(bsdir, "build_stats")
            with open(bs, "a") as f:
                rootfs = e.data.getVar('IMAGE_ROOTFS', True)
                rootfs_size = subprocess.Popen(["du", "-sh", rootfs], stdout=subprocess.PIPE).stdout.read()
                f.write("Uncompressed Rootfs size: %s" % rootfs_size)

    elif isinstance(e, bb.build.TaskFailed):
        bn = get_bn(e)
        bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
        taskdir = os.path.join(bsdir, e.data.getVar('PF', True))
        write_task_data("failed", os.path.join(taskdir, e.task), e)
        ########################################################################
        # Lets make things easier and tell people where the build failed in
        # build_status. We do this here because BuildCompleted triggers no
        # matter what the status of the build actually is
        ########################################################################
        build_status = os.path.join(bsdir, "build_stats")
        with open(build_status, "a") as f:
            f.write(e.data.expand("Failed at: ${PF} at task: %s \n" % e.task))
}

addhandler run_buildstats
run_buildstats[eventmask] = "bb.event.BuildStarted bb.event.BuildCompleted bb.build.TaskStarted bb.build.TaskSucceeded bb.build.TaskFailed"

