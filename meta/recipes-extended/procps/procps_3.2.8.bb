require procps.inc

PR = "r12"

SRC_URI += "file://procmodule.patch \
            file://psmodule.patch \
            file://linux-limits.patch \
            file://sysctl.conf \
            file://procps-3.2.8+gmake-3.82.patch \
            file://gnu-kbsd-version.patch \
            file://60_linux_version_init.patch \
            file://procps-3.2.7-top-remcpu.patch \
            file://procps-3.2.8-ps-cgroup.patch \
            file://detect_bitness.patch \
            file://0001-Fix-musl-build-failure.patch \
           "

SRC_URI[md5sum] = "9532714b6846013ca9898984ba4cd7e0"
SRC_URI[sha256sum] = "11ed68d8a4433b91cd833deb714a3aa849c02aea738c42e6b4557982419c1535"

EXTRA_OEMAKE = 'CFLAGS="${CFLAGS} -I${STAGING_INCDIR}" \
                CPPFLAGS=-I${STAGING_INCDIR} \
                LDFLAGS="${LDFLAGS}" \
                CURSES=-lncurses \
                install="install -D" \
                ldconfig=echo'

do_install_append () {
	install -d ${D}${sysconfdir}
	install -m 0644 ${WORKDIR}/sysctl.conf ${D}${sysconfdir}/sysctl.conf
	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -d ${D}${sysconfdir}/sysctl.d
		ln -sf ../sysctl.conf ${D}${sysconfdir}/sysctl.d/99-sysctl.conf
	fi
}

CONFFILES_${PN} = "${sysconfdir}/sysctl.conf"
