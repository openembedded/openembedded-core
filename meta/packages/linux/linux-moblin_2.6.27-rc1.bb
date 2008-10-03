require linux-moblin.inc

PR = "r3"

DEFAULT_PREFERENCE_eee901 = "1"

SRC_URI = "${KERNELORG_MIRROR}pub/linux/kernel/v2.6/linux-2.6.26.tar.bz2 \
		${KERNELORG_MIRROR}pub/linux/kernel/v2.6/testing/patch-2.6.27-rc1.bz2;patch=1 \
		file://0001_Export_shmem_file_setup_for_DRM-GEM.patch;patch=1 \
	   	file://0002_i915.Use_more_consistent_names_for_regs.patch;patch=1 \
		file://0003_i915.Add_support_for_MSI_and_interrupt_mitigation.patch;patch=1 \
		file://0004_i915.Track_progress_inside_of_batchbuffers_for_determining_wedgedness.patch;patch=1 \
		file://0005_i915.remove_settable_use_mi_batchbuffer_start.patch;patch=1 \
		file://0006_i915.Ignore_X_server_provided_mmio_address.patch;patch=1 \
		file://0007_i915.Initialize_hardware_status_page_at_device_load_when_possible.patch;patch=1 \
		file://0008_drm.Add_GEM_graphics_execution_manager_to_i915_driver.patch;patch=1 \
		file://0009-squashfs3.3-2.6.27.patch;patch=1 \
		file://0010_unionfs-2.4_for_2.6.27-rc1.patch;patch=1 \
		file://0011_workaround_unidef_step.patch;patch=1 \
		file://0012_intelfb_945gme.patch;patch=1 \
		file://defconfig-eee901"

S = "${WORKDIR}/linux-2.6.26"
