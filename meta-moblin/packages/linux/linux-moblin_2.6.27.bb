require linux-moblin.inc

PR = "r8"
PE = "1"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_netbook = "1"
DEFAULT_PREFERENCE_menlow = "1"

SRC_URI = "${KERNELORG_MIRROR}pub/linux/kernel/v2.6/linux-2.6.27.tar.bz2 \
		file://0001-drm-remove-define-for-non-linux-systems.patch;patch=1 \
		file://0002-i915-remove-settable-use_mi_batchbuffer_start.patch;patch=1 \
		file://0003-i915-Ignore-X-server-provided-mmio-address.patch;patch=1 \
		file://0004-i915-Use-more-consistent-names-for-regs-and-store.patch;patch=1 \
		file://0005-i915-Add-support-for-MSI-and-interrupt-mitigation.patch;patch=1 \
		file://0006-i915-Track-progress-inside-of-batchbuffers-for-dete.patch;patch=1 \
		file://0007-i915-Initialize-hardware-status-page-at-device-load.patch;patch=1 \
		file://0008-Add-Intel-ACPI-IGD-OpRegion-support.patch;patch=1 \
		file://0009-drm-fix-sysfs-error-path.patch;patch=1 \
		file://0010-i915-separate-suspend-resume-functions.patch;patch=1 \
		file://0011-drm-vblank-rework.patch;patch=1 \
		file://0012-Export-shmem_file_setup-for-DRM-GEM.patch;patch=1 \
		file://0013-Export-kmap_atomic_pfn-for-DRM-GEM.patch;patch=1 \
		file://0014-drm-Add-GEM-graphics-execution-manager-to-i915.patch;patch=1 \
		file://0015-i915-Add-chip-set-ID-param.patch;patch=1 \
		file://0016-i915-Use-struct_mutex-to-protect-ring-in-GEM-mode.patch;patch=1 \
		file://0017-i915-Make-use-of-sarea_priv-conditional.patch;patch=1 \
		file://0018-i915-gem-install-and-uninstall-irq-handler-in-enter.patch;patch=1 \
		file://0019-DRM-Return-EBADF-on-bad-object-in-flink-and-retur.patch;patch=1 \
		file://0020-drm-Avoid-oops-in-GEM-execbuffers-with-bad-argument.patch;patch=1 \
		file://0021-drm-G33-class-hardware-has-a-newer-965-style-MCH-n.patch;patch=1 \
		file://0022-drm-use-ioremap_wc-in-i915-instead-of-ioremap.patch;patch=1 \
		file://0023-drm-clean-up-many-sparse-warnings-in-i915.patch;patch=1 \
		file://0024-fastboot-create-a-asynchronous-initlevel.patch;patch=1 \
		file://0025-fastboot-turn-the-USB-hostcontroller-initcalls-into.patch;patch=1 \
		file://0026-fastboot-convert-a-few-non-critical-ACPI-drivers-to.patch;patch=1 \
		file://0027-fastboot-hold-the-BKL-over-the-async-init-call-sequ.patch;patch=1 \
		file://0028-fastboot-sync-the-async-execution-before-late_initc.patch;patch=1 \
		file://0029-fastboot-make-fastboot-a-config-option.patch;patch=1 \
		file://0030-fastboot-retry-mounting-the-root-fs-if-we-can-t-fin.patch;patch=1 \
		file://0031-fastboot-make-the-raid-autodetect-code-wait-for-all.patch;patch=1 \
		file://0032-fastboot-remove-wait-for-all-devices-before-mounti.patch;patch=1 \
		file://0033-fastboot-make-the-RAID-autostart-code-print-a-messa.patch;patch=1 \
		file://0034-fastboot-fix-typo-in-init-Kconfig-text.patch;patch=1 \
		file://0035-fastboot-remove-duplicate-unpack_to_rootfs.patch;patch=1 \
		file://0036-warning-fix-init-do_mounts_md-c.patch;patch=1 \
		file://0037-init-initramfs.c-unused-function-when-compiling-wit.patch;patch=1 \
		file://0038-fastboot-fix-blackfin-breakage-due-to-vmlinux.lds-c.patch;patch=1 \
		file://0039-Add-a-script-to-visualize-the-kernel-boot-process.patch;patch=1 \
		file://0040-fastboot-fix-issues-and-improve-output-of-bootgraph.patch;patch=1 \
		file://0041-r8169-8101e.patch;patch=1 \
		file://0042-intelfb-945gme.patch;patch=1 \
		file://0043-superreadahead-patch.patch;patch=1 \
		file://defconfig-menlow \
		file://defconfig-netbook"

SRC_URI_append_menlow = " file://psb-driver.patch;patch=1"

S = "${WORKDIR}/linux-2.6.27"
