cml1_do_configure() {
	set -e
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake oldconfig
}

EXPORT_FUNCTIONS do_configure
addtask configure after do_unpack do_patch before do_compile

inherit terminal

OE_TERMINAL_EXPORTS += "HOST_EXTRACFLAGS HOSTLDFLAGS TERMINFO CROSS_CURSES_LIB CROSS_CURSES_INC"
HOST_EXTRACFLAGS = "${BUILD_CFLAGS} ${BUILD_LDFLAGS}"
HOSTLDFLAGS = "${BUILD_LDFLAGS}"
CROSS_CURSES_LIB = "-lncurses -ltinfo"
CROSS_CURSES_INC = '-DCURSES_LOC="<curses.h>"'
TERMINFO = "${STAGING_DATADIR_NATIVE}/terminfo"

KCONFIG_CONFIG_COMMAND ??= "menuconfig"
python do_menuconfig() {
    import shutil

    try:
        mtime = os.path.getmtime(".config")
        shutil.copy(".config", ".config.orig")
    except OSError:
        mtime = 0

    # We need to know when the command completes but some terminals (including gnome-terminal
    # and tmux) gives us no way to do this. We therefore write the pid to a temporal file
    # then monitor the pid until it exits.
    import tempfile
    pidfile = tempfile.NamedTemporaryFile(delete = False).name
    try:
        oe_terminal("${SHELL} -c \"echo $$ > %s; make %s; if [ \$? -ne 0 ]; then echo 'Command failed.'; printf 'Press any key to continue... '; read r; fi\"" % (pidfile, d.getVar('KCONFIG_CONFIG_COMMAND')),
                d.getVar('PN') + ' Configuration', d)
        while os.stat(pidfile).st_size <= 0:
            continue
        with open(pidfile, "r") as f:
            pid = int(f.readline())
    finally:
        os.unlink(pidfile)

    import time
    while True:
        try:
            os.kill(pid, 0)
            time.sleep(0.1)
        except OSError:
            break

    # FIXME this check can be removed when the minimum bitbake version has been bumped
    if hasattr(bb.build, 'write_taint'):
        try:
            newmtime = os.path.getmtime(".config")
        except OSError:
            newmtime = 0

        if newmtime > mtime:
            bb.note("Configuration changed, recompile will be forced")
            bb.build.write_taint('do_compile', d)
}
do_menuconfig[depends] += "ncurses-native:do_populate_sysroot"
do_menuconfig[nostamp] = "1"
do_menuconfig[dirs] = "${B}"
addtask menuconfig after do_configure

python do_diffconfig() {
    import shutil
    import subprocess

    workdir = d.getVar('WORKDIR')
    fragment = workdir + '/fragment.cfg'
    configorig = '.config.orig'
    config = '.config'

    try:
        md5newconfig = bb.utils.md5_file(configorig)
        md5config = bb.utils.md5_file(config)
        isdiff = md5newconfig != md5config
    except IOError as e:
        bb.fatal("No config files found. Did you do menuconfig ?\n%s" % e)

    if isdiff:
        statement = 'diff --unchanged-line-format= --old-line-format= --new-line-format="%L" ' + configorig + ' ' + config + '>' + fragment
        subprocess.check_call(statement, shell=True)

        shutil.copy(configorig, config)

        bb.plain("Config fragment has been dumped into:\n %s" % fragment)
    else:
        if os.path.exists(fragment):
            os.unlink(fragment)
}

do_diffconfig[nostamp] = "1"
do_diffconfig[dirs] = "${B}"
addtask diffconfig
