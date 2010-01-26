require linux-moblin.inc

PR = "r0"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_netbook = "1"
#DEFAULT_PREFERENCE_menlow = "1"

SRC_URI = "${KERNELORG_MIRROR}/pub/linux/kernel/v2.6/linux-2.6.31.5.tar.bz2 \
           file://linux-2.6-build-nonintconfig.patch;patch=1 \
           file://linux-2.6.32-cpuidle.patch;patch=1 \
           file://linux-2.6.32-n_tty-honor-opost-flag-for-echoes.patch;patch=1 \
           file://linux-2.6.32-n_tty-move-echoctl-check-and-clean-up-logic.patch;patch=1 \
           file://linux-2.6.33-pit-fix.patch;patch=1 \
           file://linux-2.6.29-dont-wait-for-mouse.patch;patch=1 \
           file://linux-2.6.29-sreadahead.patch;patch=1 \
           file://linux-2.6.29-kms-edid-cache.patch;patch=1 \
           file://linux-2.6.29-kms-run-async.patch;patch=1 \
           file://linux-2.6.29-kms-dont-blank-display.patch;patch=1 \
           file://linux-2.6.29-kms-after-sata.patch;patch=1 \
           file://linux-2.6.30-non-root-X.patch;patch=1 \
           file://linux-2.6.31-drm-kms-flip.patch;patch=1 \
           file://linux-2.6.31-drm-mem-info.patch;patch=1 \
           file://linux-2.6.31-drm-i915-fix.patch;patch=1 \
           file://linux-2.6.31-drm-i915-opregion.patch;patch=1 \
           file://linux-2.6.31-drm-i915-vblank-fix.patch;patch=1 \
           file://linux-2.6.29-silence-acer-message.patch;patch=1 \
           file://linux-2.6.31-silence-wacom.patch;patch=1 \
           file://linux-2.6.29-jbd-longer-commit-interval.patch;patch=1 \
           file://linux-2.6.29-touchkit.patch;patch=1 \
           file://linux-2.6.31-1-2-timberdale.patch;patch=1 \
           file://linux-2.6.31-2-2-timberdale.patch;patch=1 \
           file://linux-2.6-driver-level-usb-autosuspend.patch;patch=1 \
           file://linux-2.6.31-bluetooth-suspend.patch;patch=1 \
           file://linux-2.6-usb-uvc-autosuspend.patch;patch=1 \
           file://linux-2.6.31-samsung.patch;patch=1 \
           file://MRST-GFX-driver-consolidated.patch;patch=1 \
           file://linux-2.6.31-iegd.patch;patch=1 \
           file://linux-2.6.32-acpi-cstate-fixup.patch;patch=1 \
           file://linux-2.6.32-timer-fix.patch;patch=1 \
           file://linux-2.6.33-copy-checks.patch;patch=1 \
           file://close_debug_info_of_rt2860.patch;patch=1 \
#           file://i915_split.patch.patch;patch=1 \
#           file://defconfig-menlow \
           file://defconfig-netbook"

S = "${WORKDIR}/linux-2.6.31.5"
