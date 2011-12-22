SUMMARY = "The basic file, shell and text manipulation utilities."
DESCRIPTION = "The GNU Core Utilities provide the basic file, shell and text \
manipulation utilities. These are the core utilities which are expected to exist on \
every system."
HOMEPAGE = "http://www.gnu.org/software/coreutils/"
BUGTRACKER = "http://debbugs.gnu.org/coreutils"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504\
                    file://src/ls.c;startline=5;endline=16;md5=e1a509558876db58fb6667ba140137ad"
PR = "r3"
DEPENDS = "gmp libcap"
DEPENDS_virtclass-native = ""

inherit autotools gettext

SRC_URI = "${GNU_MIRROR}/coreutils/${BP}.tar.xz \
           file://remove-usr-local-lib-from-m4.patch \
          "
SRC_URI[md5sum] = "bcb135ce553493a45aba01b39eb3920a"
SRC_URI[sha256sum] = "0d120817c19292edb19e92ae6b8eac9020e03d51e0af9cb116cf82b65d18b02d"

EXTRA_OECONF_virtclass-native = "--without-gmp"

# [ df mktemp base64 gets a special treatment and is not included in this
bindir_progs = "basename chcon cksum comm csplit cut dir dircolors dirname du \
                env expand expr factor fmt fold groups head hostid id install \
                join link logname md5sum mkfifo nice nl nohup nproc od paste pathchk \
                pinky pr printenv printf ptx readlink runcon seq sha1sum sha224sum sha256sum \
                sha384sum sha512sum shred shuf sort split stat stdbuf sum tac tail tee test timeout\
                tr truncate tsort tty unexpand uniq unlink uptime users vdir wc who whoami yes"

# hostname gets a special treatment and is not included in this
base_bindir_progs = "cat chgrp chmod chown cp date dd echo false kill ln ls mkdir \
                     mknod mv pwd rm rmdir sleep stty sync touch true uname"

sbindir_progs= "chroot"

do_install_append() {
	for i in ${bindir_progs} df mktemp base64; do mv ${D}${bindir}/$i ${D}${bindir}/$i.${PN}; done

	install -d ${D}${base_bindir}
	for i in ${base_bindir_progs}; do mv ${D}${bindir}/$i ${D}${base_bindir}/$i.${PN}; done

	install -d ${D}${sbindir}
	for i in ${sbindir_progs}; do mv ${D}${bindir}/$i ${D}${sbindir}/$i.${PN}; done

	# [ requires special handling because [.coreutils will cause the sed stuff
	# in update-alternatives to fail, therefore use lbracket - the name used
	# for the actual source file.
	mv ${D}${bindir}/[ ${D}${bindir}/lbracket.${PN}
	install -d ${D}${libdir}/coreutils
	mv ${D}${libexecdir}/coreutils/libstdbuf.so ${D}${libdir}/coreutils
}

pkg_postinst_${PN} () {
	for i in ${bindir_progs}; do update-alternatives --install ${bindir}/$i $i $i.${PN} 100; done

	for i in ${base_bindir_progs}; do update-alternatives --install ${base_bindir}/$i $i $i.${PN} 100; done

	for i in ${sbindir_progs}; do update-alternatives --install ${sbindir}/$i $i $i.${PN} 100; done

	# Special cases. [ needs to be treated separately.
	update-alternatives --install '${bindir}/[' '[' 'lbracket.${PN}' 100
	
	# Special cases. base64, mktemp and df need to be treated separately, because busybox have them in base_binding not bindir
	update-alternatives --install ${base_bindir}/base64 base64 ${bindir}/base64.${PN} 100;
	update-alternatives --install ${base_bindir}/mktemp mktemp ${bindir}/mktemp.${PN} 100;
	update-alternatives --install ${base_bindir}/df df ${bindir}/df.${PN} 100;
}

pkg_prerm_${PN} () {
	for i in ${bindir_progs}; do update-alternatives --remove $i $i.${PN}; done

	for i in ${base_bindir_progs}; do update-alternatives --remove $i $i.${PN}; done

	for i in ${sbindir_progs}; do update-alternatives --remove $i $i.${PN}; done

	# The special cases
	update-alternatives --remove hostname hostname.${PN}
	update-alternatives --remove uptime uptime.${PN}
	update-alternatives --remove '[' 'lbracket.${PN}'
	update-alternatives --remove base64 ${bindir}/base64.${PN}
	update-alternatives --remove mktemp ${bindir}/mktemp.${PN}
	update-alternatives --remove df ${bindir}.df.${PN}
}

BBCLASSEXTEND = "native"
