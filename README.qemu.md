QEMU Emulation Targets
======================

To simplify development, the build system supports building images to
work with the QEMU emulator in system emulation mode. Our support and testing
is currently focused around the following primary architectures and machine
targets:

* ARM (qemuarm + qemuarm64)
* x86 (qemux86 + qemux86-64)
* RISC-V (qemuriscv64)

Use of the QEMU images is covered in the Yocto Project Reference Manual.
The appropriate MACHINE variable value corresponding to the target is given
in brackets.

OpenEmbedded does also have support for PowerPC (qemuppc/qemuppc64), MIPS
(qemumips + qemumips64), qemuriscv32 and other less common targets but these
are not as widely tested.
