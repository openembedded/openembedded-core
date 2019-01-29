#!/usr/bin/env python

import sys
import os
import re
from . import ftools

# A parser that can be used to identify weather a line is a test result or a section statement.
class Lparser(object):

    def __init__(self, **kwargs):

        self.test_regex = {}
        self.test_regex[0] = {}
        self.test_regex[0]['pass'] = re.compile(r"^PASS:(.+)")
        self.test_regex[0]['fail'] = re.compile(r"^FAIL:(.+)")
        self.test_regex[0]['skip'] = re.compile(r"^SKIP:(.+)")

        self.section_regex = {}
        self.section_regex[0] = {}
        self.section_regex[0]['begin'] = re.compile(r"^BEGIN: .*/(.+)/ptest")
        self.section_regex[0]['end'] = re.compile(r"^END: .*/(.+)/ptest")

    # Parse a line and return a tuple containing the type of result (test/section) and its category, status and name
    def parse_line(self, line):

        for test_category, test_status_list in self.test_regex.items():
            for test_status, status_regex in test_status_list.items():
                test_name = status_regex.search(line)
                if test_name:
                    return ['test', test_category, test_status, test_name.group(1)]

        for section_category, section_status_list in self.section_regex.items():
            for section_status, status_regex in section_status_list.items():
                section_name = status_regex.search(line)
                if section_name:
                    return ['section', section_category, section_status, section_name.group(1)]
        return None


class Result(object):

    def __init__(self):
        self.result_dict = {}

    def store(self, section, test, status):
        if not section in self.result_dict:
            self.result_dict[section] = []

        self.result_dict[section].append((test, status))

    # sort tests by the test name(the first element of the tuple), for each section. This can be helpful when using git to diff for changes by making sure they are always in the same order.
    def sort_tests(self):
        for package in self.result_dict:
            sorted_results = sorted(self.result_dict[package], key=lambda tup: tup[0])
            self.result_dict[package] = sorted_results

    # Log the results as files. The file name is the section name and the contents are the tests in that section.
    def log_as_files(self, target_dir, test_status):
        status_regex = re.compile('|'.join(map(str, test_status)))
        if not type(test_status) == type([]):
            raise Exception("test_status should be a list. Got " + str(test_status) + " instead.")
        if not os.path.exists(target_dir):
            raise Exception("Target directory does not exist: %s" % target_dir)

        for section, test_results in self.result_dict.items():
            prefix = ''
            for x in test_status:
                prefix +=x+'.'
            if section:
                prefix += section
            section_file = os.path.join(target_dir, prefix)
            # purge the file contents if it exists
            open(section_file, 'w').close()
            for test_result in test_results:
                (test_name, status) = test_result
                # we log only the tests with status in the test_status list
                match_status = status_regex.search(status)
                if match_status:
                    ftools.append_file(section_file, status + ": " + test_name)

    # Not yet implemented!
    def log_to_lava(self):
        pass
