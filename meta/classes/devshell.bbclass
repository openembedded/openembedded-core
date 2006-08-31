EXTRA_OEMAKE[export] = 1

do_devshell[dirs] = "${S}"
do_devshell[nostamp] = 1
do_devshell[interactive] = 1
devshell_do_devshell() {
	bash -i
}
addtask devshell after do_patch


EXPORT_FUNCTIONS do_devshell

