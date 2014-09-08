SUMMARY = "Provide limited super user privileges to specific users"
DESCRIPTION = "Sudo (superuser do) allows a system administrator to give certain users (or groups of users) the ability to run some (or all) commands as root while logging all commands and arguments."
HOMEPAGE = "http://www.sudo.ws"
BUGTRACKER = "http://www.sudo.ws/bugs/"
SECTION = "admin"
LICENSE = "ISC & BSD & Zlib"
LIC_FILES_CHKSUM = "file://doc/LICENSE;md5=69e337d679950e304953813158595256 \
                    file://plugins/sudoers/redblack.c;beginline=1;endline=41;md5=e2dbb155fc49beea947515300bab99e0 \
                    file://compat/fnmatch.c;beginline=3;endline=27;md5=67f83ee9bd456557397082f8f1be0efd \
                    file://compat/getcwd.c;beginline=5;endline=27;md5=449af4cc57fc7d46f42090608ba3e681 \
                    file://compat/glob.c;beginline=6;endline=31;md5=5872733146b9eb0deb79e1f664815b85 \
                    file://compat/snprintf.c;beginline=6;endline=31;md5=c98b24f02967c095d7a70ae2e4d4d4ea \
                    file://include/queue.h;beginline=5;endline=27;md5=449af4cc57fc7d46f42090608ba3e681 \
                    file://compat/inet_pton.c;beginline=3;endline=17;md5=3970ab0518ab79cbd0bafb697f10b33a"

inherit autotools

PACKAGECONFIG ??= ""
PACKAGECONFIG[zlib] = "--enable-zlib,--disable-zlib,zlib"

CONFFILES_${PN} = "${sysconfdir}/sudoers"

EXTRA_OECONF = "--with-editor=/bin/vi --with-env-editor"

do_configure_prepend () {
	if [ ! -e ${S}/acinclude.m4 ]; then
		cat ${S}/aclocal.m4 > ${S}/acinclude.m4
	fi
}

# mksigname/mksiglist are used on build host to generate source files
do_compile_prepend () {
	oe_runmake SSP_CFLAGS="" SSP_LDFLAGS="" CC="$BUILD_CC" CFLAGS="$BUILD_CFLAGS" CPPFLAGS="$BUILD_CPPFLAGS -I${S}/include -I${S} -I${B}"  -C compat mksigname mksiglist
}

# Explicitly create ${localstatedir}/lib before do_install to ensure
# the directory is accessible by all users. Otherwise the mkinstalldirs
# script (from sudo) will recursively create ${localstatedir}/lib/sudo
# and then chmod each directory with 0700 permissions, which isn't what
# we want (i.e, users would not be able to access /var/lib).
do_install_prepend (){
	mkdir -p ${D}/${localstatedir}/lib
}
