#!/bin/bash
gradle --version
gradle build --continuous --console=plain -x test &
gradle bootRun --console=plain