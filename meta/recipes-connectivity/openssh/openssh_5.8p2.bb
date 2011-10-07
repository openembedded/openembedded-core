SUMMARY = "Secure rlogin/rsh/rcp/telnet replacement"
DESCRIPTION = "Secure rlogin/rsh/rcp/telnet replacement (OpenSSH) \
Ssh (Secure Shell) is a program for logging into a remote machine \
and for executing commands on a remote machine."
HOMEPAGE = "http://openssh.org"
SECTION = "console/network"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENCE;md5=bae9a689be41581503bcf95d8fb42c4e"

PR = "r2"

DEPENDS = "zlib openssl"
DEPENDS += "${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"

RPROVIDES = "ssh sshd"

CONFLICTS_${PN} = "dropbear"
RCONFLICTS_${PN}-sshd = "dropbear"
RCONFLICTS_${PN}-keygen = "ssh-keygen"

SRC_URI = "ftp://ftp.openbsd.org/pub/OpenBSD/OpenSSH/portable/openssh-${PV}.tar.gz \
           file://nostrip.patch \
           file://sshd_config \
           file://ssh_config \
           file://init \
           ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)}"

PAM_SRC_URI = "file://sshd"
SRC_URI[md5sum] = "0541579adf9d55abb15ef927048d372e"
SRC_URI[sha256sum] = "5c35ec7c966ce05cc4497ac59c0b54a556e55ae7368165cc8c4129694654f314"

inherit useradd update-rc.d

USERADD_PACKAGES = "${PN}-sshd"
USERADD_PARAM_${PN}-sshd = "--system --no-create-home --home-dir /var/run/sshd --shell /bin/false --user-group sshd"
INITSCRIPT_PACKAGES = "${PN}-sshd"
INITSCRIPT_NAME_${PN}-sshd = "sshd"
INITSCRIPT_PARAMS_${PN}-sshd = "defaults 9"

inherit autotools

# LFS support:
CFLAGS += "-D__FILE_OFFSET_BITS=64"
export LD = "${CC}"

EXTRA_OECONF = "--with-rand-helper=no \
                ${@base_contains('DISTRO_FEATURES', 'pam', '--with-pam', '--without-pam', d)} \
                --without-zlib-version-check \
                --with-privsep-path=/var/run/sshd \
                --sysconfdir=${sysconfdir}/ssh \
                --with-xauth=/usr/bin/xauth"

# This is a workaround for uclibc because including stdio.h
# pulls in pthreads.h and causes conflicts in function prototypes.
# This results in compilation failure, so unless this is fixed,
# disable pam for uclibc.
EXTRA_OECONF_append_libc-uclibc=" --without-pam"

do_configure_prepend () {
	if [ ! -e acinclude.m4 -a -e aclocal.m4 ]; then
		cp aclocal.m4 acinclude.m4
	fi
}

do_compile_append () {
	install -m 0644 ${WORKDIR}/sshd_config ${S}/
	install -m 0644 ${WORKDIR}/ssh_config ${S}/
}

do_install_append () {
	for i in ${DISTRO_FEATURES};
	do
		if [ ${i} = "pam" ];  then
			install -d ${D}${sysconfdir}/pam.d
			install -m 0755 ${WORKDIR}/sshd ${D}${sysconfdir}/pam.d/sshd
		fi
	done
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/sshd
	mv ${D}${bindir}/scp ${D}${bindir}/scp.${PN}
	mv ${D}${bindir}/ssh ${D}${bindir}/ssh.${PN}
	rm -f ${D}${bindir}/slogin ${D}${datadir}/Ssh.bin
	rmdir ${D}/var/run/sshd ${D}/var/run ${D}/var
}

ALLOW_EMPTY_${PN} = "1"

PACKAGES =+ "${PN}-keygen ${PN}-scp ${PN}-ssh ${PN}-sshd ${PN}-sftp ${PN}-misc ${PN}-sftp-server"
FILES_${PN}-scp = "${bindir}/scp.${PN}"
FILES_${PN}-ssh = "${bindir}/ssh.${PN} ${sysconfdir}/ssh/ssh_config"
FILES_${PN}-sshd = "${sbindir}/sshd ${sysconfdir}/init.d/sshd"
FILES_${PN}-sshd += "${sysconfdir}/ssh/moduli ${sysconfdir}/ssh/sshd_config"
FILES_${PN}-sftp = "${bindir}/sftp"
FILES_${PN}-sftp-server = "${libexecdir}/sftp-server"
FILES_${PN}-misc = "${bindir}/ssh* ${libexecdir}/ssh*"
FILES_${PN}-keygen = "${bindir}/ssh-keygen"

RDEPENDS_${PN} += "${PN}-scp ${PN}-ssh ${PN}-sshd ${PN}-keygen"
DEPENDS_${PN}-sshd += "update-rc.d"
RDEPENDS_${PN}-sshd += "update-rc.d ${PN}-keygen"

pkg_postinst_${PN}-scp () {
	update-alternatives --install ${bindir}/scp scp scp.${PN} 90
}

pkg_postinst_${PN}-ssh () {
	update-alternatives --install ${bindir}/ssh ssh ssh.${PN} 90
}

pkg_postrm_${PN}-ssh () {
	update-alternatives --remove ${bindir}/ssh ssh.${PN}
}

pkg_postrm_${PN}-scp () {
	update-alternatives --remove ${bindir}/scp scp.${PN}
}

CONFFILES_${PN}-sshd = "${sysconfdir}/ssh/sshd_config"
CONFFILES_${PN}-ssh = "${sysconfdir}/ssh/ssh_config"
