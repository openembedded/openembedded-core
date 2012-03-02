inherit cross

DEPENDS = "virtual/${TARGET_PREFIX}binutils virtual/${TARGET_PREFIX}libc-for-gcc ${NATIVEDEPS}"
PROVIDES = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++"

require gcc-configure-cross.inc
require gcc-package-cross.inc

do_compile () {
	oe_runmake all-host all-target-libgcc
	# now generate script to drive testing
	echo "#!/usr/bin/env sh" >${B}/${TARGET_PREFIX}testgcc
	set >> ${B}/${TARGET_PREFIX}testgcc
	# prune out the unneeded vars
	sed -i -e "/^BASH/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^USER/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^OPT/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^DIRSTACK/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^EUID/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^FUNCNAME/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^GROUPS/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^HOST/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^HOME/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^IFS/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^LC_ALL/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^LOGNAME/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^MACHTYPE/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^OSTYPE/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^PIPE/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^SHELL/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^'/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^UID/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^TERM/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^PATCH_GET/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^PKG_/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^POSIXLY_/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^PPID/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^PS4/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^Q/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^SHLVL/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^STAGING/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^LD_LIBRARY_PATH/d" ${B}/${TARGET_PREFIX}testgcc
	sed -i -e "/^PSEUDO/d" ${B}/${TARGET_PREFIX}testgcc

	# append execution part of the script
cat >> ${B}/${TARGET_PREFIX}testgcc << STOP
target="\$1"
shift
usage () {
	echo "Usage:"
	echo "\$0 user@target 'extra options to dejagnu'"
	echo "\$0 target 'extra options to dejagnu'"
	echo "\$0 target"
	echo "e.g. \$0 192.168.7.2 ' dg.exp=visibility-d.c'"
	echo "will only run visibility-d.c test case"
	echo "e.g. \$0 192.168.7.2 '/-mthumb dg.exp=visibility-d.c'"
	echo "will only run visibility-d.c test case in thumb mode"
	echo "You need to have dejagnu autogen expect installed"
	echo "on the build host"
    }
if [ "x\$target" = "x" ]
then
	echo "Please specify the target machine and remote user in form of user@target"
	usage
	exit 1;
fi

echo "\$target" | grep "@" 2>&1 > /dev/null
if [ "x\$?" = "x0" ]
then
   user=\$(echo \$target | cut -d '@' -f 1)
   target=\$(echo \$target | cut -d '@' -f 2)
else
   user=\$USER
fi
ssh \$user@\$target date 2>&1 > /dev/null
if [ "x\$?" != "x0" ]
then
	echo "Failed connecting to \$user@\$target it could be because"
	echo "you don't have passwordless ssh setup to access \$target"
	echo "or sometimes host key has been changed"
	echo "in such case do something like below on build host"
	echo "ssh-keygen -f "~/.ssh/known_hosts" -R \$target"
	echo "and then try ssh \$user@\$target"

	usage
	exit 1
fi
	echo "lappend boards_dir [pwd]/../../.." > ${B}/site.exp
	echo "load_generic_config \"unix\"" > ${B}/${PACKAGE_ARCH}.exp
	echo "set_board_info username \$user" >> ${B}/${PACKAGE_ARCH}.exp
	echo "set_board_info rsh_prog ssh" >> ${B}/${PACKAGE_ARCH}.exp
	echo "set_board_info rcp_prog scp" >> ${B}/${PACKAGE_ARCH}.exp
	echo "set_board_info hostname \$target" >> ${B}/${PACKAGE_ARCH}.exp
	DEJAGNU=${B}/site.exp make -k check RUNTESTFLAGS="--target_board=${PACKAGE_ARCH}\$@"

STOP

	chmod +x ${B}/${TARGET_PREFIX}testgcc

}
