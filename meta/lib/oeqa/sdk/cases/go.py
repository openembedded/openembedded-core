#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import os
import shutil
import unittest

from oeqa.core.utils.path import remove_safe
from oeqa.sdk.case import OESDKTestCase

from oeqa.utils.subprocesstweak import errors_have_output
errors_have_output()

class GoCompileTest(OESDKTestCase):
    td_vars = ['MACHINE', 'TARGET_ARCH']

    @classmethod
    def setUpClass(self):
        # Copy test.go file to SDK directory (same as GCC test uses files_dir)
        shutil.copyfile(os.path.join(self.tc.files_dir, 'test.go'),
                        os.path.join(self.tc.sdk_dir, 'test.go'))

    def setUp(self):
        target_arch = self.td.get("TARGET_ARCH")
        # Check for go-cross-canadian package (uses target architecture)
        if not self.tc.hasHostPackage("go-cross-canadian-%s" % target_arch):
            raise unittest.SkipTest("GoCompileTest class: SDK doesn't contain a Go cross-canadian toolchain")

        # Additional runtime check for go command availability
        try:
            self._run('which go')
        except Exception as e:
            raise unittest.SkipTest("GoCompileTest class: go command not available: %s" % str(e))

    def test_go_build(self):
        """Test Go build command (native compilation)"""
        self._run('cd %s; go build -o test test.go' % self.tc.sdk_dir)

    def test_go_module(self):
        """Test Go module creation and building"""
        # Create a simple Go module
        self._run('cd %s; go mod init hello-go' % self.tc.sdk_dir)
        self._run('cd %s; go build -o hello-go' % self.tc.sdk_dir)

    @classmethod
    def tearDownClass(self):
        files = [os.path.join(self.tc.sdk_dir, f) \
                for f in ['test.go', 'test', 'hello-go', 'go.mod', 'go.sum']]
        for f in files:
            remove_safe(f)

class GoHostCompileTest(OESDKTestCase):
    td_vars = ['MACHINE', 'SDK_SYS', 'TARGET_ARCH']

    # Architecture mapping from Yocto/Poky to Go
    ARCH_MAP = {
        'aarch64': 'arm64',
        'cortexa57': 'arm64',  # ARM Cortex-A57 is ARM64
        'cortexa72': 'arm64',  # ARM Cortex-A72 is ARM64
        'cortexa53': 'arm64',  # ARM Cortex-A53 is ARM64
        'x86_64': 'amd64',
        'i586': '386',
        'i686': '386',
        'mips': 'mips',
        'mipsel': 'mipsle',
        'powerpc64': 'ppc64',
        'powerpc64le': 'ppc64le',
        'riscv64': 'riscv64',
    }

    @classmethod
    def setUpClass(self):
        # Copy test.go file to SDK directory (same as GCC test uses files_dir)
        shutil.copyfile(os.path.join(self.tc.files_dir, 'test.go'),
                        os.path.join(self.tc.sdk_dir, 'test.go'))

    def setUp(self):
        target_arch = self.td.get("TARGET_ARCH")
        # Check for go-cross-canadian package (uses target architecture)
        if not self.tc.hasHostPackage("go-cross-canadian-%s" % target_arch):
            raise unittest.SkipTest("GoHostCompileTest class: SDK doesn't contain a Go cross-canadian toolchain")

        # Additional runtime check for go command availability
        try:
            self._run('which go')
        except Exception as e:
            raise unittest.SkipTest("GoHostCompileTest class: go command not available: %s" % str(e))

    def _get_go_arch(self):
        """Get Go architecture from SDK_SYS"""
        sdksys = self.td.get("SDK_SYS")
        arch = sdksys.split('-')[0]

        # Handle ARM variants
        if arch.startswith('arm'):
            return 'arm'

        # Use mapping for other architectures
        return self.ARCH_MAP.get(arch, arch)

    def test_go_cross_compile(self):
        """Test Go cross-compilation for target"""
        goarch = self._get_go_arch()
        self._run('cd %s; GOOS=linux GOARCH=%s go build -o test-%s test.go' % (self.tc.sdk_dir, goarch, goarch))

    def test_go_module_cross_compile(self):
        """Test Go module cross-compilation"""
        goarch = self._get_go_arch()
        self._run('cd %s; go mod init hello-go' % self.tc.sdk_dir)
        self._run('cd %s; GOOS=linux GOARCH=%s go build -o hello-go-%s' % (self.tc.sdk_dir, goarch, goarch))

    @classmethod
    def tearDownClass(self):
        # Clean up files with dynamic architecture names
        files = [os.path.join(self.tc.sdk_dir, f) \
                for f in ['test.go', 'go.mod', 'go.sum']]
        # Add architecture-specific files using the same mapping
        for arch in self.ARCH_MAP.values():
            files.extend([os.path.join(self.tc.sdk_dir, f) \
                         for f in ['test-%s' % arch, 'hello-go-%s' % arch]])
        # Add 'arm' for ARM variants
        files.extend([os.path.join(self.tc.sdk_dir, f) \
                     for f in ['test-arm', 'hello-go-arm']])
        for f in files:
            remove_safe(f)
