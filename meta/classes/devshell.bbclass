do_devshell[dirs] = "${S}"
do_devshell[nostamp] = "1"

XAUTHORITY ?= "${HOME}/.Xauthority"

devshell_do_devshell() {
	export DISPLAY='${DISPLAY}'
	export DBUS_SESSION_BUS_ADDRESS='${DBUS_SESSION_BUS_ADDRESS}'
	export XAUTHORITY='${XAUTHORITY}'
	export TERMWINDOWTITLE="Bitbake Developer Shell"
	export EXTRA_OEMAKE='${EXTRA_OEMAKE}'
	export SHELLCMDS="bash"
	${TERMCMDRUN}
	if [ $? -ne 0 ]; then
	    echo "Fatal: '${TERMCMD}' not found. Check TERMCMD variable."
	    exit 1
	fi
}
addtask devshell after do_patch

EXPORT_FUNCTIONS do_devshell

