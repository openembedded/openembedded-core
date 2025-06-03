#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

# fitImage kernel compression algorithm
FIT_KERNEL_COMP_ALG ?= "gzip"
FIT_KERNEL_COMP_ALG_EXTENSION ?= ".gz"

# Kernel image type passed to mkimage (i.e. kernel kernel_noload...)
UBOOT_MKIMAGE_KERNEL_TYPE ?= "kernel"

uboot_prep_kimage() {
	output_dir=$1
	# For backward compatibility with kernel-fitimage.bbclass and kernel-uboot.bbclass
	# support calling without parameter as well
	if [ -z "$output_dir" ]; then
		output_dir='.'
	fi

	linux_bin=$output_dir/linux.bin
	if [ -e "arch/${ARCH}/boot/compressed/vmlinux" ]; then
		vmlinux_path="arch/${ARCH}/boot/compressed/vmlinux"
		linux_suffix=""
		linux_comp="none"
	elif [ -e "arch/${ARCH}/boot/vmlinuz.bin" ]; then
		rm -f "$linux_bin"
		cp -l "arch/${ARCH}/boot/vmlinuz.bin" "$linux_bin"
		vmlinux_path=""
		linux_suffix=""
		linux_comp="none"
	else
		vmlinux_path="vmlinux"
		# Use vmlinux.initramfs for $linux_bin when INITRAMFS_IMAGE_BUNDLE set
		# As per the implementation in kernel.bbclass.
		# See do_bundle_initramfs function
		if [ "${INITRAMFS_IMAGE_BUNDLE}" = "1" ] && [ -e vmlinux.initramfs ]; then
			vmlinux_path="vmlinux.initramfs"
		fi
		linux_suffix="${FIT_KERNEL_COMP_ALG_EXTENSION}"
		linux_comp="${FIT_KERNEL_COMP_ALG}"
	fi

	[ -n "$vmlinux_path" ] && ${KERNEL_OBJCOPY} -O binary -R .note -R .comment -S "$vmlinux_path" "$linux_bin"

	if [ "$linux_comp" != "none" ] ; then
		if [ "$linux_comp" = "gzip" ] ; then
			gzip -9 "$linux_bin"
		elif [ "$linux_comp" = "lzo" ] ; then
			lzop -9 "$linux_bin"
		elif [ "$linux_comp" = "lzma" ] ; then
			xz --format=lzma -f -6 "$linux_bin"
		fi
		mv -f "$linux_bin$linux_suffix" "$linux_bin"
	fi

	printf "$linux_comp" > "$output_dir/linux_comp"
}

def uboot_prep_kimage_py(d):
    import subprocess

    arch = d.getVar('ARCH')
    initramfs_image_bundle = d.getVar('INITRAMFS_IMAGE_BUNDLE')
    fit_kernel_comp_alg = d.getVar('FIT_KERNEL_COMP_ALG') or 'gzip'
    fit_kernel_comp_alg_extension = d.getVar('FIT_KERNEL_COMP_ALG_EXTENSION') or '.gz'
    kernel_objcopy = d.getVar('KERNEL_OBJCOPY')

    vmlinux_path = ""
    linux_suffix = ""
    linux_comp = "none"

    if os.path.exists(f'arch/{arch}/boot/compressed/vmlinux'):
        vmlinux_path = f'arch/{arch}/boot/compressed/vmlinux'
    elif os.path.exists(f'arch/{arch}/boot/vmlinuz.bin'):
        if os.path.exists('linux.bin'):
            os.remove('linux.bin')
        os.link(f'arch/{arch}/boot/vmlinuz.bin', 'linux.bin')
    else:
        vmlinux_path = 'vmlinux'
        # Use vmlinux.initramfs for linux.bin when INITRAMFS_IMAGE_BUNDLE set
        # As per the implementation in kernel.bbclass.
        # See do_bundle_initramfs function
        if initramfs_image_bundle == '1' and os.path.exists('vmlinux.initramfs'):
            vmlinux_path = 'vmlinux.initramfs'
        linux_suffix = fit_kernel_comp_alg_extension
        linux_comp = fit_kernel_comp_alg

    if vmlinux_path:
        subprocess.run([kernel_objcopy.strip(), '-O', 'binary', '-R', '.note', '-R', '.comment', '-S', os.path.abspath(vmlinux_path), 'linux.bin'], check=True)
        # if ret.returncode != 0:
        # bb.fatal(f"Error: stderr: {ret.stderr.decode('utf-8')}   stdout: {ret.stdout.decode('utf-8')}, vmlinux_path: {os.path.abspath(vmlinux_path)}, pwd: {os.getcwd()}, args: {ret.args}")

    if linux_comp != "none":
        if linux_comp == "gzip":
            subprocess.run(['gzip', '-9', 'linux.bin'], check=True)
        elif linux_comp == "lzo":
            subprocess.run(['lzop', '-9', 'linux.bin'], check=True)
        elif linux_comp == "lzma":
            subprocess.run(['xz', '--format=lzma', '-f', '-6', 'linux.bin'], check=True)
        os.rename(f'linux.bin{linux_suffix}', 'linux.bin')

    return linux_comp
