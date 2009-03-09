require linux-moblin.inc

PR = "r3"
PE = "1"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_netbook = "1"
DEFAULT_PREFERENCE_menlow = "1"

SRC_URI = "${KERNELORG_MIRROR}pub/linux/kernel/v2.6/linux-2.6.28.tar.bz2 \
           ${KERNELORG_MIRROR}pub/linux/kernel/v2.6/testing/patch-2.6.29-rc2.bz2;patch=1 \
           file://0001-fastboot-retry-mounting-the-root-fs-if-we-can-t-fin.patch;patch=1 \
           file://0002-fastboot-remove-wait-for-all-devices-before-mounti.patch;patch=1 \
           file://0003-fastboot-remove-duplicate-unpack_to_rootfs.patch;patch=1 \
           file://0004-superreadahead-patch.patch;patch=1 \
           file://0005-fastboot-async-enable-default.patch;patch=1 \
           file://0006-Revert-drm-i915-GEM-on-PAE-has-problems-disable.patch;patch=1 \
           file://0007-acer-error-msg.patch;patch=1 \
           file://defconfig-menlow \
           file://defconfig-netbook"

SRC_URI_append_menlow = " file://i915_split.patch;patch=1 file://psb-driver.patch;patch=1"

S = "${WORKDIR}/linux-2.6.28"
