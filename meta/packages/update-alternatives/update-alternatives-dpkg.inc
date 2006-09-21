LICENSE = "GPL"
SECTION = "base"
SRC_URI = "${DEBIAN_MIRROR}/main/d/dpkg/dpkg_${PV}.tar.gz"
S = "${WORKDIR}/dpkg-${PV}"
PACKAGE_ARCH = "all"

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
