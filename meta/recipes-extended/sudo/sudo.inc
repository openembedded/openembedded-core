DESCRIPTION = "Sudo (superuser do) allows a system administrator to give certain users (or groups of users) the ability to run some (or all) commands as root while logging all commands and arguments."
HOMEPAGE = "http://www.courtesan.com/sudo/"
BUGTRACKER = "http://www.sudo.ws/bugs/"
PRIORITY = "optional"
SECTION = "admin"
LICENSE = "ISC & UCB & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a7dfe8895011d65d0c2e24aaf5ad0843 \
                    file://nonunix.h;startline=4;endline=28;md5=1e70feedac93a3fd7f5254e3fec52677 \
                    file://vasgroups.c;startline=4;endline=28;md5=1e70feedac93a3fd7f5254e3fec52677 \
                    file://fnmatch.c;startline=6;endline=31;md5=0779058eafd6e23b966585b45bfa54f3 \
                    file://getcwd.c;startline=5;endline=27;md5=08d82914995224a0ca42116d7ca2a218 \
                    file://glob.c;startline=6;endline=31;md5=299cb38ec8d56e89118ce57fb83b4f78 \
                    file://snprintf.c;startline=6;endline=31;md5=dabd56a89a7a773850dc06ee4f1ecde2"

inherit autotools

EXTRA_OECONF = "--with-editor=/bin/vi --with-env-editor"

do_configure_prepend () {
	# Prevent binaries from being stripped on the host
	sed -i 's/\($(INSTALL).*\) -s \(.*[(sudo|visudo)]\)/\1 \2/g' Makefile.in

	rm -f acsite.m4
	if [ ! -e acinclude.m4 ]; then
		cat aclocal.m4 > acinclude.m4
	fi
}

pkg_postinst() {
	if [ "x$D" != "x" ]; then
		exit 1
	fi

	chmod 4111 /usr/bin/sudo
	chmod 0440 /etc/sudoers
}
