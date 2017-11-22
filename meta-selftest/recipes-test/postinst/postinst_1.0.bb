SUMMARY = "Packages to exercise postinstall functions"
LICENSE = "MIT"

inherit allarch

PACKAGES = "${PN}-rootfs ${PN}-delayed-a ${PN}-delayed-b"

ALLOW_EMPTY_${PN}-rootfs = "1"
ALLOW_EMPTY_${PN}-delayed-a = "1"
ALLOW_EMPTY_${PN}-delayed-b = "1"

RDEPENDS_${PN}-delayed-a = "${PN}-rootfs"
RDEPENDS_${PN}-delayed-b = "${PN}-delayed-a"

TESTDIR = "${sysconfdir}/postinst-test"

# At rootfs time touch $TESTDIR/rootfs.  Errors if the file already exists, or
# if the function runs on first boot.
pkg_postinst_${PN}-rootfs () {
    set -e

    if [ -z "$D" ]; then
        echo "${PN}-rootfs should have finished at rootfs time"
        exit 1
    fi

    if [ -e $D${TESTDIR}/rootfs ]; then
        echo "$D${TESTDIR}/rootfs exists, but should not"
        exit 1
    fi

    mkdir -p $D${TESTDIR}
    touch $D${TESTDIR}/rootfs
}

# Depends on rootfs, delays until first boot, verifies that the rootfs file was
# written.
pkg_postinst_${PN}-delayed-a () {
    set -e

    if [ -n "$D" ]; then
        echo "Delaying ${PN}-delayed-a until first boot"
        exit 1
    fi

    if [ ! -e ${TESTDIR}/rootfs ]; then
        echo "${PN}-delayed-a: ${TESTDIR}/rootfs not found"
        exit 1
    fi

    touch ${TESTDIR}/delayed-a
}

# Depends on delayed-a, delays until first boot, verifies that the delayed-a file was
# written. This verifies the ordering between delayed postinsts.
pkg_postinst_${PN}-delayed-b () {
    set -e

    if [ -n "$D" ]; then
        echo "Delaying ${PN}-delayed-b until first boot"
        exit 1
    fi

    if [ ! -e ${TESTDIR}/delayed-a ]; then
        echo "${PN}-delayed-b: ${TESTDIR}/delayed-a not found"
        exit 1
    fi

    touch ${TESTDIR}/delayed-b
}
