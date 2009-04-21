require linux-moblin.inc

PR = "r10"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_netbook = "1"
DEFAULT_PREFERENCE_menlow = "1"

SRC_URI = "${KERNELORG_MIRROR}pub/linux/kernel/v2.6/linux-2.6.29.1.tar.bz2 \
           file://linux-2.6-build-nonintconfig.patch;patch=1 \
           file://linux-2.6.29-retry-root-mount.patch;patch=1 \
           file://linux-2.6.29-dont-wait-for-mouse.patch;patch=1 \
           file://linux-2.6.29-fast-initrd.patch;patch=1 \
           file://linux-2.6.29-sreadahead.patch;patch=1 \
           file://linux-2.6.29-enable-async-by-default.patch;patch=1 \
           file://linux-2.6.29-drm-revert.patch;patch=1 \
           file://linux-2.6.19-modesetting-by-default.patch;patch=1 \
           file://linux-2.6.29-fast-kms.patch;patch=1 \
           file://linux-2.6.29-even-faster-kms.patch;patch=1 \
           file://linux-2.6.29-silence-acer-message.patch;patch=1 \
           file://linux-2.6.29-input-introduce-a-tougher-i8042.reset.patch;patch=1 \
           file://linux-2.6.29-msiwind.patch;patch=1 \
           file://linux-2.6.29-flip-ide-net.patch;patch=1 \
           file://linux-2.6.29-kms-after-sata.patch;patch=1 \
           file://linux-2.6.29-jbd-longer-commit-interval.patch;patch=1 \
           file://linux-2.6.29-touchkit.patch;patch=1 \
           file://linux-2.6.30-fix-async.patch;patch=1 \
           file://linux-2.6.30-fix-suspend.patch;patch=1 \
           file://0001-drm-Split-out-the-mm-declarations-in-a-separate-hea.patch;patch=1 \
           file://0002-drm-Add-a-tracker-for-global-objects.patch;patch=1 \
           file://0003-drm-Export-hash-table-functionality.patch;patch=1 \
           file://0007-drm-Add-unlocked-IOCTL-functionality-from-the-drm-r.patch;patch=1 \
           file://linux-2.6.29-psb-driver.patch;patch=1 \
           file://linux-2.6.29-psb-S0i1_and_S0i3_OSPM_support.patch;patch=1 \
           file://linux-2.6.29-e100-add-support-for-82552-10-100-adapter.patch;patch=1 \
           file://linux-2.6.29-pnv-agp.patch;patch=1 \
           file://linux-2.6.29-pnv-drm.patch;patch=1 \
           file://linux-2.6.29-pnv-fix-gtt-size.patch;patch=1 \
           file://linux-2.6.29-pnv-fix-i2c.patch;patch=1 \
           file://linux-2.6.29-drm-i915-Fix-LVDS-dither-setting.patch;patch=1 \
           file://linux-2.6.29-timberdale.patch;patch=1 \
#           file://i915_split.patch;patch=1 \
           file://defconfig-menlow \
           file://defconfig-netbook"

S = "${WORKDIR}/linux-2.6.29.1"
