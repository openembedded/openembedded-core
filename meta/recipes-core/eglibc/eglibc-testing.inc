do_compile_append () {
	# now generate script to drive testing
	echo "#!/usr/bin/env sh" >${B}/${HOST_PREFIX}testeglibc
	set >> ${B}/${HOST_PREFIX}testeglibc
	# prune out the unneeded vars
	sed -i -e "/^BASH/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^USER/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^OPT/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^DIRSTACK/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^EUID/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^FUNCNAME/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^GROUPS/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^HOST/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^HOME/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^IFS/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^LC_ALL/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^LOGNAME/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^MACHTYPE/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^OSTYPE/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^PIPE/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^SHELL/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^'/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^UID/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^TERM/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^PATCH_GET/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^PKG_/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^POSIXLY_/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^PPID/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^PS4/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^Q/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^SHLVL/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^STAGING/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^LD_LIBRARY_PATH/d" ${B}/${HOST_PREFIX}testeglibc
	sed -i -e "/^PSEUDO/d" ${B}/${HOST_PREFIX}testeglibc
	# append execution part script
cat >> ${B}/${HOST_PREFIX}testeglibc << STOP
target="\$1"
if [ "x\$target" = "x" ]
then
	echo "Please specify the target machine and remote user in for of user@target"
	exit 1;
fi
wrapper="${S}/scripts/cross-test-ssh.sh \$target"
localedef="${STAGING_BINDIR_NATIVE}/cross-localedef --little-endian --uint32-align=4"
make cross-localedef="\$localedef" cross-test-wrapper="\$wrapper" -k check
STOP
}
