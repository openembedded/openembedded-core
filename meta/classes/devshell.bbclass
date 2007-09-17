EXTRA_OEMAKE[export] = "1"

do_devshell[dirs] = "${S}"
do_devshell[nostamp] = "1"

devshell_do_devshell() {
	export TERMWINDOWTITLE="Bitbake Developer Shell"
	${TERMCMD}
	if [ $? -ne 0 ]; then
	    echo "Fatal: '${TERMCMD}' not found. Check TERMCMD variable."
	    exit 1
	fi
}
addtask devshell after do_patch


EXPORT_FUNCTIONS do_devshell

