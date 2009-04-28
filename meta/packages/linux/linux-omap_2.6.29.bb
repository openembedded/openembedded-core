require linux.inc

DESCRIPTION = "Linux kernel for OMAP processors"
KERNEL_IMAGETYPE = "uImage"

COMPATIBLE_MACHINE = "omap5912osk|omap1710h3|omap2430sdp|omap2420h4|beagleboard|omap3evm|omap3-pandora|overo|omapzoom"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_overo = "1"
DEFAULT_PREFERENCE_beagleboard = "1"

SRCREV = "58cf2f1425abfd3a449f9fe985e48be2d2555022"

# The main PR is now using MACHINE_KERNEL_PR, for omap3 see conf/machine/include/omap3.inc
PR_append = "+gitr${SRCREV}"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/tmlind/linux-omap-2.6.git;protocol=git \
	   file://defconfig"

SRC_URI_append = " \
           file://no-empty-flash-warnings.patch;patch=1 \
           file://no-cortex-deadlock.patch;patch=1 \
           file://read_die_ids.patch;patch=1 \
           file://fix-install.patch;patch=1 \
           file://dss2/0001-Revert-gro-Fix-legacy-path-napi_complete-crash.patch;patch=1 \
           file://dss2/0002-OMAPFB-move-omapfb.h-to-include-linux.patch;patch=1 \
           file://dss2/0003-DSS2-OMAP2-3-Display-Subsystem-driver.patch;patch=1 \
           file://dss2/0004-DSS2-OMAP-framebuffer-driver.patch;patch=1 \
           file://dss2/0005-DSS2-Add-panel-drivers.patch;patch=1 \
           file://dss2/0006-DSS2-HACK-Add-DSS2-support-for-N800.patch;patch=1 \
           file://dss2/0007-DSS2-Add-DSS2-support-for-SDP-Beagle-Overo-EVM.patch;patch=1 \
           file://dss2/0008-DSS2-Add-function-to-display-object-to-get-the-back.patch;patch=1 \
           file://dss2/0009-DSS2-Add-acx565akm-panel.patch;patch=1 \
           file://dss2/0010-DSS2-Small-VRFB-context-allocation-bug-fixed.patch;patch=1 \
           file://dss2/0011-DSS2-Allocated-memory-for-Color-Look-up-table.patch;patch=1 \
           file://dss2/0012-DSS2-Fix-DMA-rotation.patch;patch=1 \
           file://dss2/0013-DSS2-Verify-that-overlay-paddr-0.patch;patch=1 \
           file://dss2/0014-DSS2-Add-function-to-get-DSS-logic-clock-rate.patch;patch=1 \
           file://dss2/0015-DSS2-DSI-calculate-VP_CLK_RATIO-properly.patch;patch=1 \
           file://dss2/0016-DSS2-DSI-improve-packet-len-calculation.patch;patch=1 \
           file://dss2/0017-DSS2-Disable-video-planes-on-sync-lost-error.patch;patch=1 \
           file://dss2/0018-DSS2-check-for-ovl-paddr-only-when-enabling.patch;patch=1 \
           file://dss2/0019-DSS2-Check-fclk-limits-when-configuring-video-plane.patch;patch=1 \
           file://dss2/0020-DSS2-Check-scaling-limits-against-proper-values.patch;patch=1 \
           file://dss2/0021-DSS2-Add-venc-register-dump.patch;patch=1 \
           file://dss2/0022-DSS2-FB-remove-unused-var-warning.patch;patch=1 \
           file://dss2/0023-DSS2-pass-the-default-FB-color-format-through-board.patch;patch=1 \
           file://dss2/0024-DSS2-Beagle-Use-gpio_set_value.patch;patch=1 \
           file://dss2/0025-DSS2-VRFB-Macro-for-calculating-base-address-of-th.patch;patch=1 \
           file://dss2/0026-DSS2-DSI-sidlemode-to-noidle-while-sending-frame.patch;patch=1 \
           file://dss2/0027-DSS2-VRFB-rotation-and-mirroring-implemented.patch;patch=1 \
           file://dss2/0028-DSS2-OMAPFB-Added-support-for-the-YUV-VRFB-rotatio.patch;patch=1 \
           file://dss2/0029-DSS2-OMAPFB-Set-line_length-correctly-for-YUV-with.patch;patch=1 \
           file://dss2/0030-DSS2-dispc_get_trans_key-was-returning-wrong-key-ty.patch;patch=1 \
           file://dss2/0031-DSS2-do-bootmem-reserve-for-exclusive-access.patch;patch=1 \
           file://dss2/0032-DSS2-Fix-DISPC_VID_FIR-value-for-omap34xx.patch;patch=1 \
           file://dss2/0033-DSS2-Prefer-3-tap-filter.patch;patch=1 \
           file://dss2/0034-DSS2-VRAM-improve-omap_vram_add_region.patch;patch=1 \
           file://dss2/0035-DSS2-Added-the-function-pointer-for-getting-default.patch;patch=1 \
           file://dss2/0036-DSS2-Added-support-for-setting-and-querying-alpha-b.patch;patch=1 \
           file://dss2/0037-DSS2-Added-support-for-querying-color-keying.patch;patch=1 \
           file://dss2/0038-DSS2-OMAPFB-Some-color-keying-pointerd-renamed-in-D.patch;patch=1 \
           file://dss2/0039-DSS2-Add-sysfs-entry-to-for-the-alpha-blending-supp.patch;patch=1 \
           file://dss2/0040-DSS2-Provided-proper-exclusion-for-destination-colo.patch;patch=1 \
           file://dss2/0041-DSS2-Disable-vertical-offset-with-fieldmode.patch;patch=1 \
           file://dss2/0042-DSS2-Don-t-enable-fieldmode-automatically.patch;patch=1 \
           file://dss2/0043-DSS2-Swap-field-0-and-field-1-registers.patch;patch=1 \
           file://dss2/0044-DSS2-add-sysfs-entry-for-seting-the-rotate-type.patch;patch=1 \
           file://dss2/0045-DSS2-Fixed-line-endings-from-to.patch;patch=1 \
           file://dss2/0046-DSS2-DSI-decrease-sync-timeout-from-60s-to-2s.patch;patch=1 \
           file://dss2/0047-DSS2-fix-return-value-for-rotate_type-sysfs-functio.patch;patch=1 \
           file://dss2/0048-OMAP2-3-DMA-implement-trans-copy-and-const-fill.patch;patch=1 \
           file://dss2/0049-DSS2-VRAM-clear-allocated-area-with-DMA.patch;patch=1 \
           file://dss2/0050-DSS2-OMAPFB-remove-fb-clearing-code.patch;patch=1 \
           file://dss2/0051-DSS2-VRAM-use-debugfs-not-procfs.patch;patch=1 \
           file://dss2/0052-DSS2-VRAM-fix-section-mismatch-warning.patch;patch=1 \
           file://dss2/0053-DSS2-disable-LCD-DIGIT-before-resetting-DSS.patch;patch=1 \
           file://0001-board-ldp-add-regulator-info-to-get-the-microSD-slo.patch;patch=1 \
           file://fix-unaligned-access.diff;patch=1 \
           file://make-alignment-visible.diff;patch=1 \
           file://mmctiming.patch;patch=1 \
           file://ehci.patch;patch=1 \
           file://fix-audio-capture.patch;patch=1 \
           file://ads7846-detection.patch;patch=1 \
           file://musb/0001-USB-musb-only-turn-off-vbus-in-OTG-hosts.patch;patch=1 \
           file://musb/0002-USB-composite-avoid-inconsistent-lock-state.patch;patch=1 \
           file://musb/0003-USB-musb-NAK-timeout-scheme-on-bulk-RX-endpoint.patch;patch=1 \
           file://musb/0004-USB-musb-rewrite-host-periodic-endpoint-allocation.patch;patch=1 \
           file://musb/0005-USB-TWL-disable-VUSB-regulators-when-cable-unplugg.patch;patch=1 \
           file://musb/0006-USB-gadget-composite-device-level-suspend-resume-h.patch;patch=1 \
           file://musb/0007-usb-gadget-fix-ethernet-link-reports-to-ethtool.patch;patch=1 \
           file://musb/0008-usb-musb_host-minor-enqueue-locking-fix-v2.patch;patch=1 \
           file://musb/0009-usb-musb_host-fix-ep0-fifo-flushing.patch;patch=1 \
           file://musb/0010-musb-sanitize-clearing-TXCSR-DMA-bits-take-2.patch;patch=1 \
           file://musb/0011-musb-fix-isochronous-TXDMA-take-2.patch;patch=1 \
           file://musb/0012-musb-fix-possible-panic-while-resuming.patch;patch=1 \
           file://musb/0013-musb_host-refactor-musb_save_toggle-take-2.patch;patch=1 \
           file://musb/0014-musb_gadget-suppress-parasitic-TX-interrupts-with.patch;patch=1 \
           file://musb/0015-musb_gadget-fix-unhandled-endpoint-0-IRQs.patch;patch=1 \
           file://musb/0016-musb_host-factor-out-musb_ep_-get-set-_qh.patch;patch=1 \
           file://musb/0017-musb_host-refactor-URB-giveback.patch;patch=1 \
           file://musb/0018-musb-split-out-CPPI-interrupt-handler.patch;patch=1 \
           file://musb/0019-musb_host-simplify-check-for-active-URB.patch;patch=1 \
           file://musb/0020-musb_host-streamline-musb_cleanup_urb-calls.patch;patch=1 \
           file://musb/0021-twl4030-usb-fix-minor-reporting-goofage.patch;patch=1 \
           file://musb/0022-musb-use-dma-mode-1-for-TX-if-transfer-size-equals.patch;patch=1 \
           file://musb/0023-musb-add-high-bandwidth-ISO-support.patch;patch=1 \
           file://musb/0024-USB-otg-adding-nop-usb-transceiver.patch;patch=1 \
           file://musb/0025-nop-usb-xceiv-behave-when-linked-as-a-module.patch;patch=1 \
           file://musb/0026-musb-proper-hookup-to-transceiver-drivers.patch;patch=1 \
           file://musb/0027-musb-otg-timer-cleanup.patch;patch=1 \
           file://musb/0028-musb-make-initial-HNP-roleswitch-work-v2.patch;patch=1 \
           file://musb/0029-musb-support-disconnect-after-HNP-roleswitch.patch;patch=1 \
           file://isp/v4l/0001-V4L2-Add-COLORFX-user-control.patch;patch=1 \
           file://isp/v4l/0002-V4L-Int-if-v4l2_int_device_try_attach_all-requires.patch;patch=1 \
           file://isp/v4l/0003-V4L-Int-if-Dummy-slave.patch;patch=1 \
           file://isp/v4l/0004-V4L-int-device-add-support-for-VIDIOC_QUERYMENU.patch;patch=1 \
           file://isp/v4l/0005-V4L-Int-if-Add-vidioc_int_querycap.patch;patch=1 \
           file://isp/iommu/0001-omap-iommu-tlb-and-pagetable-primitives.patch;patch=1 \
           file://isp/iommu/0002-omap-iommu-omap2-architecture-specific-functions.patch;patch=1 \
           file://isp/iommu/0003-omap-iommu-omap3-iommu-device-registration.patch;patch=1 \
           file://isp/iommu/0004-omap-iommu-simple-virtual-address-space-management.patch;patch=1 \
           file://isp/iommu/0005-omap-iommu-entries-for-Kconfig-and-Makefile.patch;patch=1 \
           file://isp/iommu/0006-omap-iommu-Don-t-try-BUG_ON-in_interrupt.patch;patch=1 \
           file://isp/iommu/0007-omap-iommu-We-support-chained-scatterlists-probabl.patch;patch=1 \
           file://isp/iommu/0008-omap2-iommu-entries-for-Kconfig-and-Makefile.patch;patch=1 \
           file://isp/omap3camera/0001-omap3isp-Add-ISP-main-driver-and-register-definitio.patch;patch=1 \
           file://isp/omap3camera/0002-omap3isp-Add-ISP-MMU-wrapper.patch;patch=1 \
           file://isp/omap3camera/0003-omap3isp-Add-userspace-header.patch;patch=1 \
           file://isp/omap3camera/0004-omap3isp-Add-ISP-frontend-CCDC.patch;patch=1 \
           file://isp/omap3camera/0005-omap3isp-Add-ISP-backend-PRV-and-RSZ.patch;patch=1 \
           file://isp/omap3camera/0006-omap3isp-Add-statistics-collection-modules-H3A-and.patch;patch=1 \
           file://isp/omap3camera/0007-omap3isp-Add-CSI2-interface-support.patch;patch=1 \
           file://isp/omap3camera/0008-omap3isp-Add-ISP-tables.patch;patch=1 \
           file://isp/omap3camera/0009-omap34xxcam-Add-camera-driver.patch;patch=1 \
#           file://isp/base/0001-omap3-Add-base-address-definitions-and-resources-fo.patch;patch=1 \
#           file://isp/standalone/0001-Resizer-and-Previewer-driver-added-to-commit.patch;patch=1 \
#           file://isp/standalone/0002-Resizer-bug-fixes-on-top-of-1.0.2-release.patch;patch=1 \
           file://0124-leds-gpio-broken-with-current-git.patch;patch=1 \
           file://modedb-hd720.patch;patch=1 \
"


SRC_URI_append_beagleboard = " file://logo_linux_clut224.ppm \
			                   file://beagle-asoc.patch;patch=1 \
"

SRC_URI_append_omap3evm = " \
	file://evm-mcspi-ts.diff;patch=1 \
"



S = "${WORKDIR}/git"


module_autoload_ohci-hcd_omap5912osk = "ohci-hcd"


