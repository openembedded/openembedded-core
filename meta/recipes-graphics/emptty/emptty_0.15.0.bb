require emptty.inc

PACKAGECONFIG ?= "${@bb.utils.filter('DISTRO_FEATURES', 'pam x11', d)}"
PACKAGECONFIG[pam] = ",,libpam,pam-plugin-succeed-if"
PACKAGECONFIG[x11] = ",,virtual/libx11,util-linux-mcookie"

DEPENDS += "${@bb.utils.contains('PACKAGECONFIG', 'pam', '', 'virtual/crypt', d)}"

GO_TAGS = ""
GO_TAGS:append = "${@bb.utils.contains('PACKAGECONFIG', 'pam', '', ',nopam', d)}"
GO_TAGS:append = "${@bb.utils.contains('PACKAGECONFIG', 'x11', '', ',noxlib', d)}"

GOBUILDFLAGS:append = " -tags=${GO_TAGS}"

export GO111MODULE = "off"

inherit go

DEPENDS += "gzip"

do_install () {
    # general collateral
    install -Dm755 ${B}/${GO_BUILD_BINDIR}/emptty ${D}${bindir}/emptty
    install -d ${D}${mandir}/man1
    gzip -cn ${S}/src/${GO_IMPORT}/res/emptty.1 > ${D}${mandir}/man1/emptty.1.gz

    # pam config
    if "${@bb.utils.contains('PACKAGECONFIG','pam','true','false',d)}"
    then
        install -Dm644 ${S}/pamconf ${D}${sysconfdir}/pam.d/emptty
    fi

    # systemd init service
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}
    then
        oe_runmake -C ${S}/src/${GO_IMPORT} DESTDIR=${D} install-systemd
    fi
}

FILES:${PN} = "\
    ${systemd_system_unitdir}/emptty.service \
    ${bindir}/emptty \
    ${mandir}/man1/emptty.1.gz \
    ${sysconfdir}/pam.d/emptty \
"

RDEPENDS:${PN} += "virtual-emptty-conf"
SYSTEMD_SERVICE:${PN} = "emptty.service"

inherit systemd

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "-r nopasswdlogin"

inherit useradd
