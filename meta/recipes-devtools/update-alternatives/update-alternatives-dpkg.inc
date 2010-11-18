SUMMARY = "Manage alternatives"
DESCRIPTION = "update-alternatives creates, removes, maintains and displays information about the symbolic links \
comprising the Debian alternatives system. The Debian alternatives system attempts solve the problem of several \
programs fulfilling the same or similar functions and how they can be installed onto a single system at the same \
time."
LICENSE = "GPL"
SECTION = "base"
SRC_URI = "${DEBIAN_MIRROR}/main/d/dpkg/dpkg_${PV}.tar.gz"
S = "${WORKDIR}/dpkg-${PV}"
PACKAGE_ARCH = "all"
PR = "r2"

do_patch () {
    cat ${S}/scripts/update-alternatives.pl | \
        sed -n -e '
    /^\$admindir=.*staging/{
        x
        s/^.*$/$D=$ENV{"D"} || ""\;/;
        p;
        x;
        s,^\$admindir=.*staging.*$,$admindir="$D${localstatedir}/dpkg"\;,;
    };
    s,^\$altdir=.*$,$altdir="$D${sysconfdir}/alternatives"\;,;
    p;' > ${S}/scripts/update-alternatives
}

do_install () {
    install -d ${D}${sbindir} \
               ${D}${localstatedir}/dpkg/alternatives \
               ${D}${sysconfdir}/alternatives

    install -m 0755 scripts/update-alternatives ${D}${sbindir}/update-alternatives
}

BBCLASSEXTEND = "native"
