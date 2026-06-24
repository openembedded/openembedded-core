#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import sys
from unittest.case import TestCase

import oe.patch


class TestRunCmd(TestCase):
    def test_runcmd_preserves_argv_elements(self):
        output = oe.patch.runcmd([
            sys.executable,
            "-c",
            "import sys; print(sys.argv[1])",
            "value with spaces;$(false)*",
        ])
        self.assertEqual(output, "value with spaces;$(false)*\n")

    def test_runcmd_allows_explicit_shell(self):
        output = oe.patch.runcmd([
            "sh", "-c", 'printf "%s" "$1"', "sh", "shell value",
        ])
        self.assertEqual(output, "shell value")

    def test_runcmd_reports_exit_status(self):
        with self.assertRaises(oe.patch.CmdError) as ctx:
            oe.patch.runcmd([
                sys.executable,
                "-c",
                "raise SystemExit(7)",
            ])

        self.assertEqual(ctx.exception.status, 7)

    def test_runcmd_wraps_exec_failure(self):
        with self.assertRaises(oe.patch.CmdError) as ctx:
            oe.patch.runcmd([
                "/definitely-not-an-existing-executable",
            ])

        self.assertEqual(ctx.exception.status, 127)
