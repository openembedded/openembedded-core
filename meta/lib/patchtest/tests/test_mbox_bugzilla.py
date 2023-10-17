# Checks related to the patch's bugzilla tag
#
# Copyright (C) 2016 Intel Corporation
#
# SPDX-License-Identifier: GPL-2.0-only

import pyparsing
import base

class Bugzilla(base.Base):
    rexp_detect = pyparsing.Regex('\[\s?YOCTO.*\]')
    rexp_validation = pyparsing.Regex('\[(\s?YOCTO\s?#\s?(\d+)\s?,?)+\]')

    def test_bugzilla_entry_format(self):
        for commit in Bugzilla.commits:
            if not self.rexp_detect.search_string(commit.commit_message):
                self.skip("No bug ID found")
            elif not self.rexp_validation.search_string(commit.commit_message):
                self.fail('Bugzilla issue ID is not correctly formatted - specify it with format: "[YOCTO #<bugzilla ID>]"', commit=commit)

