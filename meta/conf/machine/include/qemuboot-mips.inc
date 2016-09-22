# For runqemu
IMAGE_CLASSES += "qemuboot"
QB_MEM = "-m 256"
QB_MACHINE = "-machine malta"
QB_KERNEL_CMDLINE_APPEND = "console=ttyS0 console=tty"
QB_OPT_APPEND = "-vga cirrus -show-cursor -usb -usbdevice tablet -device virtio-rng-pci"
QB_SYSTEM_NAME = "qemu-system-${TUNE_ARCH}"
