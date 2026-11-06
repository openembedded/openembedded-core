SUMMARY = "DNF package manager rewritten in C++, using libsolv as a dependency resolver"
DESCRIPTION = "\
    DNF5 is a command-line package manager that automates the process of \
    installing, upgrading, configuring, and removing computer programs in a \
    consistent manner. It supports RPM packages, modulemd modules, and comps \
    groups and environments. \
"
HOMEPAGE = "https://github.com/rpm-software-management/dnf5"
BUGTRACKER = "https://github.com/rpm-software-management/dnf5/issues"
CVE_PRODUCT = "rpm:dnf5 rpm-software-management:dnf5"
SECTION = "base"

LICENSE = "LGPL-2.1-or-later AND LGPL-2.0-or-later AND GPL-2.0-or-later"
LIC_FILES_CHKSUM = "\
    file://COPYING.md;md5=9733192df318d0f806fd668b92ba0ba6 \
    file://gpl-2.0.txt;md5=570a9b3749dd0463a1778803b12a6dce \
    file://lgpl-2.0.txt;md5=920be02b2cababb5f05b0b3a69d27263 \
    file://lgpl-2.1.txt;md5=4bf661c1e3793e55c8d1051bc5e0ae21 \
"

DEPENDS = "\
    fmt \
    glib-2.0 \
    json-c \
    libmodulemd \
    librepo \
    libsolv \
    libtoml11 \
    libxml2 \
    sqlite3 \
    util-linux \
"

SRC_URI = "\
    git://github.com/rpm-software-management/dnf5.git;protocol=https;branch=main;tag=${PV} \
    file://0002-cmake-set-CMP0190-to-OLD.patch \
    file://0003-Do-not-hardcode-system_cachedir.patch \
    file://0004-add-support-for-gcc-12-and-below.patch \
    file://0005-libdnf-fix-arm-arch-mapping-issues-for-qemuarmv5.patch \
    file://0006-rename-stdout-stderr-members.patch \
    file://0007-dnf5-offline-fix-build-without-systemd.patch \
"
SRC_URI:append:class-native = " file://0001-Do-not-hardcode-the-systemd-unit-directory.patch"
SRCREV = "a42787112973edb4e5ba237d4fca6f878ac5f4c1"

inherit bash-completion cmake gettext pkgconfig siteinfo systemd
inherit_defer ${@bb.utils.contains('PACKAGECONFIG', 'python3', 'python3targetconfig', '', d)}

PACKAGECONFIG[acl] = "-DWITH_ACL=ON,-DWITH_ACL=OFF,acl"
PACKAGECONFIG[dnfdaemon-client] = "-DWITH_DNF5DAEMON_CLIENT=ON,-DWITH_DNF5DAEMON_CLIENT=OFF,sdbus-c++"
PACKAGECONFIG[dnfdaemon-server] = "-DWITH_DNF5DAEMON_SERVER=ON,-DWITH_DNF5DAEMON_SERVER=OFF,sdbus-c++"
PACKAGECONFIG[plugins] = "-DWITH_DNF5_PLUGINS=ON,-DWITH_DNF5_PLUGINS=OFF,curl sdbus-c++ zlib"
PACKAGECONFIG[plugin-actions] = "-DWITH_PLUGIN_ACTIONS=ON,-DWITH_PLUGIN_ACTIONS=OFF"
PACKAGECONFIG[plugin-appstream] = "-DWITH_PLUGIN_APPSTREAM=ON,-DWITH_PLUGIN_APPSTREAM=OFF,appstream"
PACKAGECONFIG[plugin-expired-pgp-keys] = "-DWITH_PLUGIN_EXPIRED_PGP_KEYS=ON,-DWITH_PLUGIN_EXPIRED_PGP_KEYS=OFF,gnupg"
PACKAGECONFIG[plugin-local] = "-DWITH_PLUGIN_LOCAL=ON,-DWITH_PLUGIN_LOCAL=OFF"

# sdbus-c++ and journalctl integration.
PACKAGECONFIG[systemd] = "-DWITH_SYSTEMD=ON -DWITH_PLUGIN_SYSTEMD_INHIBIT=ON,-DWITH_SYSTEMD=OFF -DWITH_PLUGIN_SYSTEMD_INHIBIT=OFF,sdbus-c++"

# build options - bindings
PACKAGECONFIG[python3] = "-DWITH_PYTHON3=ON,-DWITH_PYTHON3=OFF,python3 swig-native"

# No sdbus-c++-native package.
PACKAGECONFIG:remove:class-native = "\
    dnfdaemon-client \
    dnfdaemon-server \
    plugins \
    plugin-manifest \
    systemd \
"

PACKAGE_BEFORE_PN += "\
    ${PN}-dnfdaemon-server \
    ${PN}-plugins \
    ${PN}-python3 \
    ${PN}-systemd \
"

# manpages generation requires http://www.sphinx-doc.org/
EXTRA_OECMAKE:append = " \
    -DENABLE_SOLV_FOCUSNEW=ON \
    -DWITH_DNF5_OBSOLETES_DNF=OFF \
    -DWITH_DNF5=ON \
    -DWITH_GO=OFF \
    -DWITH_HTML=OFF \
    -DWITH_LIBDNF5_CLI=ON \
    -DWITH_MAN=OFF \
    -DWITH_PERL5=OFF \
    -DWITH_PLUGIN_RHSM_CONTAINER=OFF \
    -DWITH_RUBY=OFF \
    -DWITH_TESTS=OFF \
    -DWITH_PLUGIN_MANIFEST=OFF \
"

# No sdbus-c++-native package exists so remove all options that depend on it.
EXTRA_OECMAKE:append:class-native = " \
    -DWITH_DNF5DAEMON_CLIENT=OFF \
    -DWITH_DNF5DAEMON_SERVER=OFF \
    -DWITH_DNF5_PLUGINS=OFF \
    -DWITH_PLUGIN_MANIFEST=OFF \
    -DWITH_SYSTEMD=OFF \
"

# Create a symlink called 'dnf' as 'make install' does not do it, but
# .spec file in dnf source tree does (and then Fedora and dnf documentation
# says that dnf binary is plain 'dnf').
do_install:append() {
    ln -rs ${D}/${bindir}/dnf5 ${D}/${bindir}/dnf
    install -d ${D}${sysconfdir}/dnf

    # Breaks dnf because the builddep plugin is provided in the
    # dnf-plugins-core package which is not available in oe-core.
    rm -f ${D}${datadir}/dnf5/aliases.d/compatibility-plugins.conf
}

# Direct dnf-native to read rpm configuration from our sysroot, not the one it was compiled in
do_install:append:class-native() {
    create_wrapper ${D}/${bindir}/dnf \
        SEQUOIA_CRYPTO_POLICY=${STAGING_DATADIR_NATIVE}/crypto-policies/back-ends/rpm-sequoia.config \
        RPM_CONFIGDIR=${STAGING_LIBDIR_NATIVE}/rpm \
        RPM_NO_CHROOT_FOR_SCRIPTS=1
}

do_install:append:class-nativesdk() {
    create_wrapper ${D}/${bindir}/dnf \
        RPM_CONFIGDIR=${SDKPATHNATIVE}${libdir_nativesdk}/rpm \
        RPM_NO_CHROOT_FOR_SCRIPTS=1
}

# Packages for testing purposes
PACKAGES += "${PN}-test-main ${PN}-test-dep"

FILES:${PN} += "\
    ${bindir}/dnf* \
    ${datadir}/dbus-1/system-services/org.rpm.dnf.v0.service \
    ${datadir}/dnf5 \
    ${datadir}/polkit-1/actions/org.rpm.dnf* \
    ${datadir}/polkit-1/rules.d/org.rpm.dnf* \
    ${datadir}/zsh/site-functions/_dnf5 \
    ${libdir}/dnf5 \
    ${libdir}/libdnf5* \
    ${sysconfdir}/dnf \
"

FILES:${PN}-bash-completion += "\
    ${datadir}/bash-completion/completions/dnf5 \
"

FILES:${PN}-dnfdaemon-server += "\
    ${systemd_system_unitdir}/dnf5daemon-server.service \
    ${datadir}/dbus-1/interfaces/org.rpm.dnf* \
    ${datadir}/dbus-1/system.d/org.rpm.dnf* \
"

FILES:${PN}-plugins += "\
    ${systemd_system_unitdir}/dnf-automatic.service \
    ${systemd_system_unitdir}/dnf-automatic.timer \
    ${systemd_system_unitdir}/dnf5-automatic.service \
    ${systemd_system_unitdir}/dnf5-automatic.timer \
"

FILES:${PN}-python3 += "\
    ${libdir}/${PYTHON_DIR}/site-packages/libdnf* \
"

FILES:${PN}-systemd += "\
    ${systemd_system_unitdir}/dnf5-makecache.service \
    ${systemd_system_unitdir}/dnf5-makecache.timer \
    ${systemd_system_unitdir}/dnf5-offline-transaction-cleanup.service \
    ${systemd_system_unitdir}/dnf5-offline-transaction.service \
"

SYSTEMD_AUTO_ENABLE ?= "disable"

RRECOMMENDS:${PN}:append:class-target = " bash-completion gnupg"
RRECOMMENDS:${PN}-test-main = "${PN}-test-dep"
ALLOW_EMPTY:${PN}-test-main = "1"
ALLOW_EMPTY:${PN}-test-dep = "1"

SKIP_RECIPE[dnf] ?= "${@bb.utils.contains('PACKAGE_CLASSES', 'package_rpm', '', 'does not build without package_rpm in PACKAGE_CLASSES due to disabled rpm support in libsolv', d)}"

BBCLASSEXTEND = "native nativesdk"
