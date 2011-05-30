SUMMARY = "Provide limited super user privileges to specific users"
DESCRIPTION = "Sudo (superuser do) allows a system administrator to give certain users (or groups of users) the ability to run some (or all) commands as root while logging all commands and arguments."
HOMEPAGE = "http://www.sudo.ws"
BUGTRACKER = "http://www.sudo.ws/bugs/"
PRIORITY = "optional"
SECTION = "admin"
LICENSE = "ISC & UCB & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0b07397b2fca3fb8b71f08cd85c6eb3f \
                    file://nonunix.h;beginline=4;endline=28;md5=af7d361f47ae60769cac77e4fca0dbb0 \
                    file://vasgroups.c;beginline=4;endline=28;md5=af7d361f47ae60769cac77e4fca0dbb0 \
                    file://fnmatch.c;beginline=6;endline=31;md5=5872733146b9eb0deb79e1f664815b85 \
                    file://getcwd.c;beginline=5;endline=27;md5=449af4cc57fc7d46f42090608ba3e681 \
                    file://glob.c;beginline=6;endline=31;md5=5872733146b9eb0deb79e1f664815b85 \
                    file://snprintf.c;beginline=6;endline=31;md5=c98b24f02967c095d7a70ae2e4d4d4ea"

inherit autotools

EXTRA_OECONF = "--with-editor=/bin/vi --with-env-editor"

do_configure_prepend () {
	if [ ! -e acinclude.m4 ]; then
		cat aclocal.m4 > acinclude.m4
	fi
}

# The script "mkinstalldirs" from package "sudo" will create directory 
# "/var/lib/sudo" by recursion with mode "0700" during installing files.
# That is to say, "var", "var/lib" and "var/lib/sudo" will possess access authority 
# with mode "0700". It cause that directory "var" and "var/lib" 
# can't be accessed by common user. Creating directory "/var/lib" before 
# installing files can resolve this problem.

do_install_prepend (){
	mkdir -p ${D}/${localstatedir}/lib
}
 

pkg_postinst_${PN} () {
	if [ "x$D" != "x" ]; then
		exit 1
	fi

	chmod 4111 /usr/bin/sudo
	chmod 0440 /etc/sudoers
}
