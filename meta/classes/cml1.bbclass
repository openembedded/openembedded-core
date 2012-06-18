cml1_do_configure() {
	set -e
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake oldconfig
}

EXPORT_FUNCTIONS do_configure
addtask configure after do_unpack do_patch before do_compile

inherit terminal

python do_menuconfig() {
        try:
            mtime = os.path.getmtime(".config")
        except OSError:
            mtime = 0

        oe_terminal("make menuconfig", '${PN} Configuration', d)

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
do_menuconfig[nostamp] = "1"
addtask menuconfig after do_configure

