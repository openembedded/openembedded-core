SUMMARY = "The basic file, shell and text manipulation utilities."
DESCRIPTION = "The GNU Core Utilities provide the basic file, shell and text \
manipulation utilities. These are the core utilities which are expected to exist on \
every system."

HOMEPAGE = "http://www.gnu.org/software/coreutils/"
BUGTRACKER = "http://debbugs.gnu.org/coreutils"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://src/ls.c;startline=4;endline=16;md5=482a96d4f25010a4e13f8743e0c3685e"
PR = "r2"
DEPENDS = "coreutils-native-${PV}"
DEPENDS_virtclass-native = "gettext-native"

inherit autotools gettext

SRC_URI_BASE = "${GNU_MIRROR}/coreutils/${BP}.tar.bz2 \
           file://gnulib_m4.patch \
           file://futimens.patch \
           file://coreutils-ls-x.patch \
           file://coreutils-6.9-cp-i-u.patch \
           file://coreutils-i18n.patch \
           file://coreutils-overflow.patch \
           file://coreutils-fix-install.patch \
           file://man-touch.patch"

SRC_URI = "${SRC_URI_BASE} file://fix_for_manpage_building.patch"
SRC_URI_virtclass-native = "${SRC_URI_BASE}"

SRC_URI[md5sum] = "c9607d8495f16e98906e7ed2d9751a06"
SRC_URI[sha256sum] = "89c2895ad157de50e53298b22d91db116ee4e1dd3fdf4019260254e2e31497b0"

# [ gets a special treatment and is not included in this
bindir_progs = "base64 basename cksum comm csplit cut dir dircolors dirname du \
                env expand expr factor fmt fold groups head hostid id install \
                join link logname md5sum mkfifo nice nl nohup od paste pathchk \
                pinky pr printenv printf ptx readlink seq sha1sum sha224sum sha256sum \
                sha384sum sha512sum shred shuf sort split stat sum tac tail tee test \
                tr tsort tty unexpand uniq unlink users vdir wc who whoami yes"

# hostname gets a special treatment and is not included in this
base_bindir_progs = "cat chgrp chmod chown cp date dd echo false kill ln ls mkdir \
                     mknod mv pwd rm rmdir sleep stty sync touch true uname"

sbindir_progs= "chroot"

do_install() {
	autotools_do_install

	for i in ${bindir_progs}; do mv ${D}${bindir}/$i ${D}${bindir}/$i.${PN}; done

	install -d ${D}${base_bindir}
	for i in ${base_bindir_progs}; do mv ${D}${bindir}/$i ${D}${base_bindir}/$i.${PN}; done

	install -d ${D}${sbindir}
	for i in ${sbindir_progs}; do mv ${D}${bindir}/$i ${D}${sbindir}/$i.${PN}; done

	# [ requires special handling because [.coreutils will cause the sed stuff
	# in update-alternatives to fail, therefore use lbracket - the name used
	# for the actual source file.
	mv ${D}${bindir}/[ ${D}${bindir}/lbracket.${PN}

	# hostname and uptime separated. busybox's versions are preferred
	mv ${D}${bindir}/hostname ${D}${base_bindir}/hostname.${PN}
	mv ${D}${bindir}/uptime ${D}${bindir}/uptime.${PN}
}

pkg_postinst_${PN} () {
	for i in ${bindir_progs}; do update-alternatives --install ${bindir}/$i $i $i.${PN} 100; done

	for i in ${base_bindir_progs}; do update-alternatives --install ${base_bindir}/$i $i $i.${PN} 100; done

	for i in ${sbindir_progs}; do update-alternatives --install ${sbindir}/$i $i $i.${PN} 100; done

	# Special cases. uptime and hostname is broken, prefer busybox's version. [ needs to be treated separately.
	update-alternatives --install ${bindir}/uptime uptime uptime.${PN} 10
	update-alternatives --install ${base_bindir}/hostname hostname hostname.${PN} 10
	update-alternatives --install '${bindir}/[' '[' 'lbracket.${PN}' 100
}

pkg_prerm_${PN} () {
	for i in ${bindir_progs}; do update-alternatives --remove $i $i.${PN}; done

	for i in ${base_bindir_progs}; do update-alternatives --remove $i $i.${PN}; done

	for i in ${sbindir_progs}; do update-alternatives --remove $i $i.${PN}; done

	# The special cases
	update-alternatives --remove hostname hostname.${PN}
	update-alternatives --remove uptime uptime.${PN}
	update-alternatives --remove '[' 'lbracket.${PN}'
}

BBCLASSEXTEND = "native"
